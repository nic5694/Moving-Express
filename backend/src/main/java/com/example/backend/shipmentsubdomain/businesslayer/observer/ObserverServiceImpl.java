package com.example.backend.shipmentsubdomain.businesslayer.observer;

import com.example.backend.shipmentsubdomain.datalayer.observer.Observer;
import com.example.backend.shipmentsubdomain.datalayer.observer.ObserverIdentifier;
import com.example.backend.shipmentsubdomain.datalayer.observer.ObserverRepository;
import com.example.backend.shipmentsubdomain.datalayer.observer.Permission;
import com.example.backend.shipmentsubdomain.datalayer.shipment.Shipment;
import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentRepository;
import com.example.backend.shipmentsubdomain.datamapperlayer.observer.ObserverRequestMapper;
import com.example.backend.shipmentsubdomain.datamapperlayer.observer.ObserverResponseMapper;
import com.example.backend.shipmentsubdomain.presentationlayer.observer.ObserverRequestModel;
import com.example.backend.shipmentsubdomain.presentationlayer.observer.ObserverResponseModel;
import com.example.backend.util.exceptions.InvalidInputException;
import com.example.backend.util.exceptions.ObserverCodeNotFound;
import com.example.backend.util.exceptions.ShipmentNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class ObserverServiceImpl implements ObserverService {
    private final ObserverRepository observerRepository;
    private final ObserverResponseMapper observerResponseMapper;
    private final ObserverRequestMapper observerRequestMapper;
    private final ShipmentRepository shipmentRepository;


    @Override
    public List<ObserverResponseModel> getAllObservers(String shipmentId) {

        if(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipmentId)==null){
            throw new ShipmentNotFoundException("shipmentId not found: " + shipmentId);
        }

        return observerResponseMapper.entitiesToResponseModels(observerRepository
                .findAllByShipmentIdentifier_ShipmentId(shipmentId));
    }

    @Override
    public ObserverResponseModel getObserverByObserverCode(String shipmentId,String observerCode) {

        if(!observerRepository.existsByObserverCode(observerCode))
            throw new InvalidInputException("Observer with code: " + observerCode + " does not exist");

        if(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipmentId)==null){
            throw new ShipmentNotFoundException("shipmentId not found: " + shipmentId);
        }

        return observerResponseMapper.entityToResponseModel(observerRepository
                .findObserverByObserverCode(observerCode));
    }

    @Override
    public ObserverResponseModel createObserver(ObserverRequestModel observerRequestModel, String shipmentId) {
        Shipment shipment = shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipmentId);
        if(shipment == null)
            throw new InvalidInputException("Shipment with id: " + shipmentId + " does not exist");

        Observer observer = observerRequestMapper.requestModelToEntity(observerRequestModel);

        String observerCode;
        do {
            observerCode = generateObserverCode();
        } while (observerRepository.existsByObserverCode(observerCode));

        observer.setObserverIdentifier(new ObserverIdentifier());
        observer.setName(observerRequestModel.getName());
        observer.setPermission(observerRequestModel.getPermission());
        observer.setObserverCode(observerCode);
        observer.setShipmentIdentifier(shipment.getShipmentIdentifier());
        observer = observerRepository.save(observer);

        return observerResponseMapper.entityToResponseModel(observer);
    }

    @Override
    public Void deleteObserverCode(String observerId, String shipmentId) {

        Shipment shipment = shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipmentId);

        if(shipment == null){
            throw new ShipmentNotFoundException("Shipment with id : " + shipmentId + " was not found");
        }

        Observer observerCode = observerRepository.findObserverByObserverIdentifier_ObserverId(observerId);

        if(observerCode == null){
            throw new ObserverCodeNotFound("Observer code with id : " + observerId + " was not found");
        }

        observerRepository.delete(observerCode);

        return null;
    }

    public ObserverResponseModel editObserverPermission(ObserverRequestModel observerRequestModel, String observerId,
                                              String shipmentId) {
        Observer observer = observerRepository.findByObserverIdentifier_ObserverId(observerId);
        if(observer == null)
            throw new InvalidInputException("Observer with Id: " + observerId + " does not exist");

        observer.setPermission(observerRequestModel.getPermission());
        return observerResponseMapper.entityToResponseModel(observerRepository.save(observer));
    }

    public boolean hasReadPermission(String observerCode) {
        Observer observer = observerRepository.findObserverByObserverCode(observerCode);
        return observer != null && observer.getPermission() == Permission.READ;
    }

    public boolean hasEditPermission(String observerCode) {
        Observer observer = observerRepository.findObserverByObserverCode(observerCode);
        return observer != null && observer.getPermission() == Permission.EDIT;
    }

    public boolean hasFullPermission(String observerCode) {
        Observer observer = observerRepository.findObserverByObserverCode(observerCode);
        return observer != null && observer.getPermission() == Permission.FULL;
    }

    private String generateObserverCode() {
        String characters = "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }
}
