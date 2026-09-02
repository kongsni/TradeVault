package com.tradevault.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "disclosures",
        indexes = {
                @Index(name = "idx_disclosure_ticker", columnList = "ticker"),
                @Index(name = "idx_disclosure_filing_date", columnList = "filingDate")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Disclosure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String accessionNumber;

    @Column(nullable = false, length = 10)
    private String ticker;

    @Column(nullable = false, length = 10)
    private String form; // 예: 8-K, 10-K

    @Column(nullable = false)
    private LocalDate filingDate;

    private LocalDate reportDate;

    @Column(nullable = false, length = 1000)
    private String documentUrl;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public Disclosure(String accessionNumber, String ticker, String form,
                      LocalDate filingDate, LocalDate reportDate,
                      String documentUrl, String description) {
        this.accessionNumber = accessionNumber;
        this.ticker = ticker;
        this.form = form;
        this.filingDate = filingDate;
        this.reportDate = reportDate;
        this.documentUrl = documentUrl;
        this.description = description;
    }
}