package com.example.backend.shipmentsubdomain.datalayer.observer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObserverRepository extends JpaRepository<Observer, Long> {
    Observer findObserverByObserverCode (String observerCode);

    Observer findObserverByObserverIdentifier_ObserverId(String ObserverId);

    List<Observer> findAllByShipmentIdentifier_ShipmentId(String shipmentId);

    boolean existsByObserverCode(String observerCode);

    Observer findByObserverIdentifier_ObserverId(String observerId);
}
