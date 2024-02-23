package com.example.backend.shipmentsubdomain.presentationlayer.quote;

import com.example.backend.shipmentsubdomain.businesslayer.quote.QuoteService;
import com.example.backend.shipmentsubdomain.datalayer.quote.QuoteStatus;
import com.example.backend.shipmentsubdomain.presentationlayer.event.EventRequestModel;
import com.example.backend.shipmentsubdomain.presentationlayer.event.EventResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping(path = "api/v1/movingexpress/quotes")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class QuoteController {
    private final QuoteService quoteService;
    @GetMapping()
    @PreAuthorize("hasAnyAuthority('Shipment_Estimator','Shipment_Owner', 'Shipment_Reviewer')")
    public ResponseEntity<List<QuoteResponseModel>> getAllQuotes(@RequestParam QuoteStatus quoteStatus){
        return ResponseEntity.ok().body(quoteService.getAllQuotes(quoteStatus));
    }


    @GetMapping("/{quoteId}")
    @PreAuthorize("hasAnyAuthority('Shipment_Estimator','Shipment_Owner', 'Shipment_Reviewer')")
    public ResponseEntity<QuoteResponseModel> getQuote(@PathVariable String quoteId){
        return ResponseEntity.ok().body(quoteService.getQuote(quoteId));
    }

    @PostMapping(value = "/request")
    public ResponseEntity<QuoteResponseModel> addQuote(@RequestBody QuoteRequestModel quoteRequestModel){
        return ResponseEntity.status(CREATED).body(quoteService.addQuote(quoteRequestModel));
    }

    @PostMapping(value = "/{quoteId}/events")
    @PreAuthorize("hasAnyAuthority('Shipment_Reviewer','Shipment_Estimator')")
    public ResponseEntity<EventResponseModel> createQuoteEvent(@RequestBody EventRequestModel eventRequestModel,
                                               @PathVariable String quoteId){
        switch (eventRequestModel.getEvent()){
            case "decline":
                return ResponseEntity.status(CREATED).body(quoteService.declineQuote(quoteId));
            case "accept":
                return ResponseEntity.status(CREATED).body(quoteService.acceptQuote(quoteId));
            case "convert":
                return ResponseEntity.status(CREATED).body(quoteService.convertQuoteToShipment(quoteId));
            default:
                throw new IllegalArgumentException("Unexpected event value: " + eventRequestModel.getEvent());
        }
    }
    @GetMapping("/Email/{u}")
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")
    public List<QuoteResponseModel> getQuoteByEmail(@PathVariable String u){
        return quoteService.getQuoteByEmail(u);
    }

}
