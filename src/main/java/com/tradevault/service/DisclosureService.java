package com.tradevault.service;

import com.tradevault.domain.Disclosure;
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

    // 특정 종목(예: AAPL)의 최신 공시 목록 조회
    public List<Disclosure> getDisclosuresByTicker(String ticker) {
        return disclosureRepository.findByTickerOrderByFilingDateDesc(ticker);
    }
}