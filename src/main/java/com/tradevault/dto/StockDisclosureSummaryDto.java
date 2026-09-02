package com.tradevault.dto;

import com.tradevault.domain.Disclosure;
import java.util.List;

public record StockDisclosureSummaryDto(
        String ticker,
        Double currentPrice,
        Double changePercent,
        Double highPrice,
        Double lowPrice,
        int totalDisclosuresCount,
        List<Disclosure> recentDisclosures
) {}