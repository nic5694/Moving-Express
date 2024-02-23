package com.example.backend.shipmentsubdomain.presentationlayer.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class EventResponseModel {
    private String event;
    private String resultType;
    private String href;
}
