package com.tradevault.controller;

import com.tradevault.domain.Disclosure;
import com.tradevault.dto.StockDisclosureSummaryDto;
import com.tradevault.service.DisclosureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/disclosures")
@RequiredArgsConstructor
public class DisclosureController {

    private final DisclosureService disclosureService;

    // 1. 단순 공시 목록 조회: GET http://localhost:8080/api/disclosures/AAPL
    @GetMapping("/{ticker}")
    public ResponseEntity<List<Disclosure>> getDisclosures(@PathVariable("ticker") String ticker) {
        List<Disclosure> disclosures = disclosureService.getDisclosuresByTicker(ticker.toUpperCase());
        return ResponseEntity.ok(disclosures);
    }

    // 2. 실시간 주가 + 공시 통합 요약: GET http://localhost:8080/api/disclosures/AAPL/summary
    @GetMapping("/{ticker}/summary")
    public ResponseEntity<StockDisclosureSummaryDto> getStockSummary(@PathVariable("ticker") String ticker) {
        StockDisclosureSummaryDto summary = disclosureService.getStockSummary(ticker);
        return ResponseEntity.ok(summary);
    }
}