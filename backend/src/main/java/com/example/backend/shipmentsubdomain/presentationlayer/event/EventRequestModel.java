package com.example.backend.shipmentsubdomain.presentationlayer.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EventRequestModel {
    private String event;

    // This is a workaround for a bug in Jackson 2.9.9 (where you need more than one property to make it work)
    // If another property is added to this class, this constructor can be removed and replaced with @AllArgsConstructor
    // See: https://github.com/FasterXML/jackson-databind/issues/2984
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public EventRequestModel(String event) {
        this.event = event;
    }
}
