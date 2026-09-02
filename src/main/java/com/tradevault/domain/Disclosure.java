package com.tradevault.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "disclosures", indexes = {
        @Index(name = "idx_disclosure_ticker", columnList = "ticker"),
        @Index(name = "idx_disclosure_filing_date", columnList = "filingDate")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Disclosure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String ticker;

    @Column(nullable = false, length = 10)
    private String form;

    @Column(nullable = false, unique = true, length = 30)
    private String accessionNumber;

    @Column(nullable = false)
    private LocalDate filingDate;

    private LocalDate reportDate;

    @Column(nullable = false, length = 1000)
    private String documentUrl;

    private String description;

    // 공시 발생일 기준 주가 변동률(%) 필드 추가
    private Double priceChangeRate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Disclosure(String ticker, String form, String accessionNumber,
                      LocalDate filingDate, LocalDate reportDate,
                      String documentUrl, String description,
                      Double priceChangeRate, LocalDateTime createdAt) {
        this.ticker = ticker;
        this.form = form;
        this.accessionNumber = accessionNumber;
        this.filingDate = filingDate;
        this.reportDate = reportDate;
        this.documentUrl = documentUrl;
        this.description = description;
        this.priceChangeRate = priceChangeRate;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    // 변동률 사후 업데이트용 비즈니스 메서드
    public void updatePriceChangeRate(Double rate) {
        this.priceChangeRate = rate;
    }
}