package com.tradevault.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FinnhubQuoteResponse(
        @JsonProperty("c") Double currentPrice, // 현재가
        @JsonProperty("d") Double change,       // 변동폭
        @JsonProperty("dp") Double changePercent,// 등락률(%)
        @JsonProperty("h") Double highPrice,    // 당일 고가
        @JsonProperty("l") Double lowPrice,     // 당일 저가
        @JsonProperty("o") Double openPrice,    // 시가
        @JsonProperty("pc") Double prevClose    // 전일 종가
) {}