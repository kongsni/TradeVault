package com.tradevault.service;

import com.tradevault.client.FinnhubClient;
import com.tradevault.domain.Disclosure;
import com.tradevault.dto.FinnhubQuoteResponse;
import com.tradevault.dto.StockDisclosureSummaryDto;
import com.tradevault.repository.DisclosureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DisclosureService {

    private final DisclosureRepository disclosureRepository;
    private final FinnhubClient finnhubClient;

    public List<Disclosure> getDisclosuresByTicker(String ticker) {
        return disclosureRepository.findByTickerOrderByFilingDateDesc(ticker);
    }

    public StockDisclosureSummaryDto getStockSummary(String ticker) {
        String upperTicker = ticker.toUpperCase();

        FinnhubQuoteResponse quote = finnhubClient.getQuote(upperTicker);
        List<Disclosure> allDisclosures = disclosureRepository.findByTickerOrderByFilingDateDesc(upperTicker);

        List<Disclosure> top10 = allDisclosures.stream()
                .limit(10)
                .toList();

        return new StockDisclosureSummaryDto(
                upperTicker,
                quote != null ? quote.currentPrice() : null,
                quote != null ? quote.changePercent() : null,
                quote != null ? quote.highPrice() : null,
                quote != null ? quote.lowPrice() : null,
                allDisclosures.size(),
                top10
        );
    }
}