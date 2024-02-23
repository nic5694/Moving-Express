package com.example.backend.shipmentsubdomain.businesslayer.observer;

import com.example.backend.shipmentsubdomain.presentationlayer.observer.ObserverRequestModel;
import com.example.backend.shipmentsubdomain.presentationlayer.observer.ObserverResponseModel;

import java.util.List;

public interface ObserverService {
    List<ObserverResponseModel> getAllObservers(String shipmentId);
    ObserverResponseModel getObserverByObserverCode (String shipmentId, String observerCode);
    ObserverResponseModel createObserver(ObserverRequestModel observerRequestModel, String shipmentId);

    Void deleteObserverCode (String observerId, String shipmentId);
    ObserverResponseModel editObserverPermission(ObserverRequestModel observerRequestModel, String observerId, String shipmentId);
}
