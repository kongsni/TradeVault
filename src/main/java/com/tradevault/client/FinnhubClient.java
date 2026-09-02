package com.tradevault.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class FinnhubClient {

    private final RestClient restClient;
    private final String apiKey;

    public FinnhubClient(
            @Value("${finnhub.api.base-url}") String baseUrl,
            @Value("${finnhub.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    // 특정 종목의 현재 실시간 주가(Quote) 조회
    public String getQuote(String ticker) {
        log.info(">>> Finnhub 실시간 주가 조회 요청: {}", ticker);

        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/quote")
                            .queryParam("symbol", ticker)
                            .queryParam("token", apiKey)
                            .build())
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            log.error(">>> Finnhub API 호출 실패: {}", e.getMessage(), e);
            return null;
        }
    }
}