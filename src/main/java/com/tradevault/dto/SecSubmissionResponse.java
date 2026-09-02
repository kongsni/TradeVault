package com.tradevault.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SecSubmissionResponse(
        String cik,
        String name,
        List<String> tickers,
        Filings filings
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Filings(Recent recent) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Recent(
            List<String> accessionNumber,
            List<String> filingDate,
            List<String> reportDate,
            List<String> form,
            List<String> primaryDocument,
            List<String> primaryDocDescription
    ) {}
}