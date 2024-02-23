package com.example.backend.shipmentsubdomain.presentationlayer.observer;

import com.example.backend.shipmentsubdomain.datalayer.observer.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class ObserverRequestModel {
    private String name;
    private Permission permission;
    private String observerCode;

}
