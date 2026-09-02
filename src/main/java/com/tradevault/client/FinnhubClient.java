package com.tradevault.client;

import com.tradevault.dto.FinnhubQuoteResponse;
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

    // JSON 문자열 대신 FinnhubQuoteResponse 객체로 바로 매핑하여 반환
    public FinnhubQuoteResponse getQuote(String ticker) {
        log.info(">>> Finnhub 실시간 주가 조회 요청: {}", ticker);

        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/quote")
                            .queryParam("symbol", ticker)
                            .queryParam("token", apiKey)
                            .build())
                    .retrieve()
                    .body(FinnhubQuoteResponse.class);
        } catch (Exception e) {
            log.error(">>> Finnhub API 호출 실패: {}", e.getMessage(), e);
            return null;
        }
    }
}