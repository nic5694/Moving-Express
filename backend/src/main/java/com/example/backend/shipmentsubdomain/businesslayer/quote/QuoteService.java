package com.example.backend.shipmentsubdomain.businesslayer.quote;

import com.example.backend.shipmentsubdomain.presentationlayer.quote.QuoteRequestModel;
import com.example.backend.shipmentsubdomain.presentationlayer.quote.QuoteResponseModel;
import com.example.backend.shipmentsubdomain.datalayer.quote.QuoteStatus;
import com.example.backend.shipmentsubdomain.presentationlayer.event.EventResponseModel;

import java.util.List;

public interface QuoteService {
    List<QuoteResponseModel> getAllQuotes(QuoteStatus quoteStatus);

    QuoteResponseModel getQuote(String quoteId);

    List<QuoteResponseModel> getQuoteByEmail(String email);

    QuoteResponseModel addQuote(QuoteRequestModel quoteRequest);

    EventResponseModel declineQuote(String quoteId);
    EventResponseModel acceptQuote(String quoteId);
    EventResponseModel convertQuoteToShipment(String quoteId);
}
