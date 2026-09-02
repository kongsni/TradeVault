package com.tradevault.controller;

import com.tradevault.domain.Disclosure;
import com.tradevault.service.DisclosureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disclosures")
@RequiredArgsConstructor
public class DisclosureController {

    private final DisclosureService disclosureService;

    // 예: GET http://localhost:8080/api/disclosures/AAPL
    @GetMapping("/{ticker}")
    public ResponseEntity<List<Disclosure>> getDisclosures(@PathVariable String ticker) {
        List<Disclosure> disclosures = disclosureService.getDisclosuresByTicker(ticker.toUpperCase());
        return ResponseEntity.ok(disclosures);
    }
}