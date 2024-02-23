package com.example.backend.shipmentsubdomain.presentationlayer.observer;

import com.example.backend.shipmentsubdomain.datalayer.observer.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class ObserverResponseModel {
    private String observerId;
    private String name;
    private String observerCode;
    private Permission permission;
    private String shipmentId;

}
