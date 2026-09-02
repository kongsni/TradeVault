package com.tradevault.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FinnhubQuoteResponse(
        @JsonProperty("c") Double currentPrice,
        @JsonProperty("d") Double change,
        @JsonProperty("dp") Double changePercent,
        @JsonProperty("h") Double highPrice,
        @JsonProperty("l") Double lowPrice,
        @JsonProperty("o") Double openPrice,
        @JsonProperty("pc") Double prevClose
) {
    // 소수점 둘째 자리까지 깔끔하게 반올림한 변동률 반환
    public Double getFormattedChangePercent() {
        if (changePercent == null) return 0.0;
        return Math.round(changePercent * 100.0) / 100.0;
    }
}