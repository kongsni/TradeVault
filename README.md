# 🏛️ TradeVault

> **SEC 공시 데이터(8-K)와 실시간 주가(Finnhub)를 결합한 금융 데이터 파이프라인 & 모니터링 대시보드 API**

TradeVault는 미국 증권거래위원회(SEC EDGAR)의 기업 중요 수시공시(8-K)와 Finnhub의 실시간 시장 시세 데이터를 수집·가공하여, 공시 발생 시점의 시장 영향력을 분석할 수 있도록 서빙하는 백엔드 데이터 파이프라인 시스템입니다.

---

## 🛠️ Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 4.1.1
- **Data Access:** Spring Data JPA, Hibernate 7.4
- **Database:** H2 In-Memory Database
- **HTTP Client:** Spring RestClient (Non-blocking ready HTTP Client)
- **External API:** SEC EDGAR REST API, Finnhub Stock API

---

## 🏗️ Architecture & Pipeline Flow

이종 데이터 소스(SEC EDGAR + Finnhub)를 효율적으로 조합하여 클라이언트에 제공하는 N-Tier 아키텍처입니다.

```text
[ Client (Browser / HTTP) ]
           │
           ▼
[ Presentation Layer: DisclosureController ]
    - RESTful Endpoint (/api/disclosures/{ticker}/summary)
    - 입력값 검증 및 표준 HTTP 응답 서빙
           │
           ▼
[ Business Layer: DisclosureService ]
    - 트랜잭션 범위 제어 (@Transactional)
    - Finnhub 시세 조회 및 SEC 공시 목록 오케스트레이션
    - 도메인 상태 변경 및 JPA Dirty Checking 관리
      ┌────┴────────────────────────┐
      ▼                             ▼
[ DisclosureRepository ]     [ FinnhubClient ]
    - Spring Data JPA            - Spring RestClient 기반
    - Ticker/FilingDate 인덱싱   - 외부 API Failure 격리 (Null-safe)
      │                             │
      ▼                             ▼
( H2 In-Memory DB )          ( Finnhub Stock API )

