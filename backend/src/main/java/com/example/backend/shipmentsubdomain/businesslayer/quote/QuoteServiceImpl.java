package com.example.backend.shipmentsubdomain.businesslayer.quote;

import com.example.backend.usersubdomain.buisnesslayer.UserService;
import com.example.backend.usersubdomain.datalayer.User;
import com.example.backend.usersubdomain.datalayer.UserRepository;
import com.example.backend.shipmentsubdomain.datalayer.quote.*;
import com.example.backend.shipmentsubdomain.datamapperlayer.quote.QuoteRequestMapper;
import com.example.backend.shipmentsubdomain.datamapperlayer.quote.QuoteResponseMapper;
import com.example.backend.shipmentsubdomain.datamapperlayer.shipment.AddressMapper;
import com.example.backend.shipmentsubdomain.datamapperlayer.shipment.QuoteResponseToShipmentMapper;
import com.example.backend.util.EmailUtil;
import com.example.backend.shipmentsubdomain.presentationlayer.quote.QuoteController;
import com.example.backend.shipmentsubdomain.presentationlayer.quote.QuoteRequestModel;
import com.example.backend.shipmentsubdomain.presentationlayer.quote.QuoteResponseModel;
import com.example.backend.shipmentsubdomain.presentationlayer.event.EventResponseModel;
import com.example.backend.util.exceptions.*;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.jmx.export.notification.UnableToSendNotificationException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {
    private final QuoteRepository quoteRepository;
    private final QuoteRequestMapper quoteRequestMapper;
    private final QuoteResponseMapper quoteResponseMapper;
    private final EmailUtil emailUtil;
    private final TemplateEngine templateEngine;
    
    @Generated
    String generateDeclineQuoteEmailContentString(Quote quote) {
        try {
            Context context = new Context();
            context.setVariable("quoteId", quote.getQuoteIdentifier() != null ? quote.getQuoteIdentifier().getQuoteId() : "");
            context.setVariable("pickupAddress", quote.getPickupAddress() != null ? quote.getPickupAddress() : "");
            context.setVariable("destinationAddress", quote.getDestinationAddress() != null ? quote.getDestinationAddress() : "");
            return templateEngine.process("declineQuote", context);
        } catch (Exception e) {
            throw new UnableToSendNotificationException("Unable to send email");
        }
    }
    @Override
    public QuoteResponseModel getQuote(String quoteId) {
        Quote existingQuote = quoteRepository.findByQuoteIdentifier_QuoteId(quoteId);
        if (existingQuote == null) {
            throw new QuoteNotFoundException("quoteId not found: " + quoteId);
        }
        return quoteResponseMapper.entityToResponseModel(existingQuote);
    }

    @Override
    public List<QuoteResponseModel> getQuoteByEmail(String email) {
        return quoteResponseMapper.entitiesListToResponseList(quoteRepository.findAllByContactDetailsEmailAddress(email));
    }

    @Override
    public QuoteResponseModel addQuote(QuoteRequestModel quoteRequestModel) {
        Quote quote = quoteRequestMapper.requestModelToEntity(quoteRequestModel);

        if(quoteRequestModel.getPickupNumberOfRooms()<=0){
            throw new InvalidNumberOfRoomsException("invalid pickupNumberOfRooms: "+quoteRequestModel.getPickupNumberOfRooms());
        }

        if(quoteRequestModel.getDestinationNumberOfRooms()<=0){
            throw new InvalidNumberOfRoomsException("invalid destinationNumberOfRooms: "+quoteRequestModel.getDestinationNumberOfRooms());
        }

        String pickupLocation=quoteRequestModel.getPickupStreetAddress()+", "+quoteRequestModel.getPickupCity()+", "+quoteRequestModel.getPickupCountry();
        String destinationLocation=quoteRequestModel.getDestinationStreetAddress()+", "+quoteRequestModel.getDestinationCity()+", "+quoteRequestModel.getDestinationCountry();

        if(pickupLocation.equals(destinationLocation)){
            throw new InvalidPickupAndDestinationAddressException("invalid pickup and destination address: "+pickupLocation);
        }

        PickupAddress pickupAddress = new PickupAddress(quoteRequestModel.getPickupStreetAddress(),
                quoteRequestModel.getPickupCity(),
                quoteRequestModel.getPickupCountry(),
                quoteRequestModel.getPickupPostalCode(),
                quoteRequestModel.getPickupNumberOfRooms(),
                quoteRequestModel.isPickupElevator(),
                quoteRequestModel.getPickupBuildingType());

        DestinationAddress destinationAddress = new DestinationAddress(quoteRequestModel.getDestinationStreetAddress(),
                quoteRequestModel.getDestinationCity(),
                quoteRequestModel.getDestinationCountry(),
                quoteRequestModel.getDestinationPostalCode(),
                quoteRequestModel.getDestinationNumberOfRooms(),
                quoteRequestModel.isDestinationElevator(),
                quoteRequestModel.getDestinationBuildingType());

        ContactDetails contactDetails = new ContactDetails(quoteRequestModel.getFirstName(),
                quoteRequestModel.getLastName(),
                quoteRequestModel.getEmailAddress(),
                quoteRequestModel.getPhoneNumber());

        quote.setPickupAddress(pickupAddress);
        quote.setDestinationAddress(destinationAddress);
        quote.setContactDetails(contactDetails);
        quote.setInitiationDate(LocalDateTime.now());
        quote.setQuoteStatus(QuoteStatus.PENDING);
        quote.setQuoteIdentifier(new QuoteIdentifier());
        quote.setApproximateWeight(quoteRequestModel.getApproximateWeight());
        quote.setApproximateShipmentValue(quote.getApproximateShipmentValue());

        Quote savedQuote = quoteRepository.save(quote);
        return quoteResponseMapper.entityToResponseModel(savedQuote);
    }

    @Override
    public List<QuoteResponseModel> getAllQuotes(QuoteStatus quoteStatus) {
        return quoteResponseMapper.entitiesListToResponseList(quoteRepository.findAllByQuoteStatus(quoteStatus));
    }

    @Override
    public EventResponseModel declineQuote(String quoteId) {
        Quote quote = quoteRepository.findByQuoteIdentifier_QuoteId(quoteId);
        if(quote==null){
            throw new QuoteNotFoundException("quoteId not found: "+quoteId);
        }
        quote.setQuoteStatus(QuoteStatus.DECLINED);
        quoteRepository.save(quote);
        emailUtil.SslEmail(quote.getContactDetails().getEmailAddress(), "Quote Declined", generateDeclineQuoteEmailContentString(quote));
        return EventResponseModel.builder()
                .resultType("SUCCESS")
                .event("decline")
                // Dynamically generate the link to the current quote
                .href(WebMvcLinkBuilder
                        .linkTo(QuoteController.class)
                        .slash(quoteId)
                        .withSelfRel()
                        .getHref())
                .build();
    }

    @Override
    public EventResponseModel acceptQuote(String quoteId) {
        Quote quote = quoteRepository.findByQuoteIdentifier_QuoteId(quoteId);
        if(quote==null){
            throw new QuoteNotFoundException("quoteId not found: "+quoteId);
        }
        quote.setQuoteStatus(QuoteStatus.ACCEPTED);
        quoteRepository.save(quote);
        return EventResponseModel.builder()
                .resultType("SUCCESS")
                .event("accept")
                .href(WebMvcLinkBuilder
                        .linkTo(QuoteController.class)
                        .slash(quoteId)
                        .withSelfRel()
                        .getHref())
                .build();
    }

    @Override
    public EventResponseModel convertQuoteToShipment(String quoteId) {
        Quote quote = quoteRepository.findByQuoteIdentifier_QuoteId(quoteId);
        if(quote==null){
            throw new QuoteNotFoundException("quoteId not found: "+quoteId);
        }
        quote.setQuoteStatus(QuoteStatus.CREATED);
        quoteRepository.save(quote);

        return EventResponseModel.builder()
                .resultType("SUCCESS")
                .event("convert")
                // Dynamically generate the link to the current quote
                .href(WebMvcLinkBuilder
                        .linkTo(QuoteController.class)
                        .slash(quoteId)
                        .withSelfRel()
                        .getHref())
                .build();
    }
}
