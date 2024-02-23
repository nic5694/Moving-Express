package com.example.backend.shipmentsubdomain.datalayer.quote;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Integer> {

    List<Quote> findAllByQuoteStatus(QuoteStatus quoteStatus);

    List<Quote> findAllByContactDetailsEmailAddress(String email);
    Quote findByQuoteIdentifier_QuoteId(String quoteId);
}
