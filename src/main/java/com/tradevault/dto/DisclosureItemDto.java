package com.tradevault.dto;

public record DisclosureItemDto(
        String accessionNumber,
        String form,
        String filingDate,
        String reportDate,
        String documentUrl,
        String description
) {}