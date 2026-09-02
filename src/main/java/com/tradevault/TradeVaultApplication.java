package com.tradevault;

import com.tradevault.client.FinnhubClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TradeVaultApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeVaultApplication.class, args);
    }

    // 서버 시작 시 Finnhub 주가 테스트 호출
    @Bean
    public CommandLineRunner testFinnhub(FinnhubClient finnhubClient) {
        return args -> {
            String quoteJson = finnhubClient.getQuote("AAPL");
            System.out.println(">>> Finnhub 주가 응답 결과: " + quoteJson);
        };
    }
}