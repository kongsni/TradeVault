package com.tradevault.repository;

import com.tradevault.domain.Disclosure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DisclosureRepository extends JpaRepository<Disclosure, Long> {

    boolean existsByAccessionNumber(String accessionNumber);

    Optional<Disclosure> findByAccessionNumber(String accessionNumber);

    List<Disclosure> findByTickerOrderByFilingDateDesc(String ticker);
}