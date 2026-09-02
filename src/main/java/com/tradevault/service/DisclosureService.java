package com.tradevault.service;

import com.tradevault.client.FinnhubClient;
import com.tradevault.domain.Disclosure;
import com.tradevault.dto.FinnhubQuoteResponse;
import com.tradevault.dto.StockDisclosureSummaryDto;
import com.tradevault.repository.DisclosureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DisclosureService {

    private final DisclosureRepository disclosureRepository;
    private final FinnhubClient finnhubClient;

    public List<Disclosure> getDisclosuresByTicker(String ticker) {
        return disclosureRepository.findByTickerOrderByFilingDateDesc(ticker);
    }

    @Transactional
    public StockDisclosureSummaryDto getStockSummary(String ticker) {
        String upperTicker = ticker.toUpperCase();

        // 1. 실시간 주가 조회
        FinnhubQuoteResponse quote = finnhubClient.getQuote(upperTicker);

        // 2. 종목 공시 목록 조회
        List<Disclosure> allDisclosures = disclosureRepository.findByTickerOrderByFilingDateDesc(upperTicker);

        // 3. 최신 공시에 현재 변동률 매핑 (최신 공시가 있을 경우)
        if (!allDisclosures.isEmpty() && quote != null) {
            Disclosure latestDisclosure = allDisclosures.get(0);
            latestDisclosure.updatePriceChangeRate(quote.getFormattedChangePercent());
        }

        List<Disclosure> top10 = allDisclosures.stream()
                .limit(10)
                .toList();

        return new StockDisclosureSummaryDto(
                upperTicker,
                quote != null ? quote.currentPrice() : null,
                quote != null ? quote.getFormattedChangePercent() : null,
                quote != null ? quote.highPrice() : null,
                quote != null ? quote.lowPrice() : null,
                allDisclosures.size(),
                top10
        );
    }
}