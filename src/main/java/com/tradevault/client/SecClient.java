package com.tradevault.client;

import com.tradevault.domain.Disclosure;
import com.tradevault.dto.DisclosureItemDto;
import com.tradevault.dto.SecSubmissionResponse;
import com.tradevault.repository.DisclosureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecClient implements CommandLineRunner {

    private final DisclosureRepository disclosureRepository;
    private static final String AAPL_CIK = "0000320193";

    @Override
    @Transactional
    public void run(String... args) {
        log.info(">>> SEC API 호출 및 H2 DB 적재 시작...");

        RestClient restClient = RestClient.builder()
                .baseUrl("https://data.sec.gov")
                .defaultHeader("User-Agent", "TradeVault kongsni@tradevault.com")
                .defaultHeader("Accept-Encoding", "gzip, deflate")
                .build();

        try {
            SecSubmissionResponse response = restClient.get()
                    .uri("/submissions/CIK{cik}.json", AAPL_CIK)
                    .retrieve()
                    .body(SecSubmissionResponse.class);

            if (response == null || response.filings() == null || response.filings().recent() == null) {
                log.warn("공시 데이터가 비어 있습니다.");
                return;
            }

            String ticker = (response.tickers() != null && !response.tickers().isEmpty())
                    ? response.tickers().get(0)
                    : "AAPL";

            List<DisclosureItemDto> disclosures = parseRecentFilings(response);

            // 8-K 공시 필터링 후 DB 적재 (중복 건 제외)
            int savedCount = 0;
            for (DisclosureItemDto dto : disclosures) {
                if (!"8-K".equalsIgnoreCase(dto.form())) {
                    continue;
                }

                if (!disclosureRepository.existsByAccessionNumber(dto.accessionNumber())) {
                    Disclosure entity = Disclosure.builder()
                            .accessionNumber(dto.accessionNumber())
                            .ticker(ticker)
                            .form(dto.form())
                            .filingDate(LocalDate.parse(dto.filingDate()))
                            .reportDate(dto.reportDate() != null && !dto.reportDate().isBlank()
                                    ? LocalDate.parse(dto.reportDate()) : null)
                            .documentUrl(dto.documentUrl())
                            .description(dto.description())
                            .build();

                    disclosureRepository.save(entity);
                    savedCount++;
                }
            }

            log.info(">>> H2 DB 적재 완료: 신규 8-K 공시 {}건 저장됨.", savedCount);
            log.info(">>> 현재 DB 총 공시 수: {}건", disclosureRepository.count());

        } catch (Exception e) {
            log.error(">>> SEC 데이터 수집 및 DB 저장 실패: {}", e.getMessage(), e);
        }
    }

    private List<DisclosureItemDto> parseRecentFilings(SecSubmissionResponse response) {
        SecSubmissionResponse.Recent recent = response.filings().recent();
        List<DisclosureItemDto> list = new ArrayList<>();

        int totalCount = recent.accessionNumber() != null ? recent.accessionNumber().size() : 0;
        String rawCik = response.cik().replaceFirst("^0+(?!$)", "");

        for (int i = 0; i < totalCount; i++) {
            String accNum = recent.accessionNumber().get(i);
            String rawAccNum = accNum.replace("-", "");
            String primaryDoc = recent.primaryDocument().get(i);

            String docUrl = String.format("https://www.sec.gov/Archives/edgar/data/%s/%s/%s",
                    rawCik, rawAccNum, primaryDoc);

            list.add(new DisclosureItemDto(
                    accNum,
                    recent.form().get(i),
                    recent.filingDate().get(i),
                    recent.reportDate().get(i),
                    docUrl,
                    recent.primaryDocDescription().get(i)
            ));
        }
        return list;
    }
}