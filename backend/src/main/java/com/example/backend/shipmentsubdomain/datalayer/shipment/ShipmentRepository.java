package com.example.backend.shipmentsubdomain.datalayer.shipment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Integer>{
    List<Shipment> findShipmentByUserId(String userId);
    List<Shipment> findShipmentByEmail(String email);
    Shipment findShipmentByShipmentIdentifier_ShipmentId(String shipmentId);
    List<Shipment> findAllByDriverId(String driverId);
    List<Shipment> findAllByDriverIdIsNull();
}
