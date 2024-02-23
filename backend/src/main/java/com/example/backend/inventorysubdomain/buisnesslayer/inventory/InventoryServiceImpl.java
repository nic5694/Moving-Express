package com.example.backend.inventorysubdomain.buisnesslayer.inventory;

import com.example.backend.inventorysubdomain.buisnesslayer.Item.ItemService;
import com.example.backend.inventorysubdomain.datalayer.Item.Item;
import com.example.backend.inventorysubdomain.datalayer.Item.ItemRepository;
import com.example.backend.inventorysubdomain.datalayer.inventory.Inventory;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryIdentifier;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryRepository;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryStatus;
import com.example.backend.inventorysubdomain.datamapperlayer.inventory.InventoryRequestMapper;
import com.example.backend.inventorysubdomain.datamapperlayer.inventory.InventoryResponseMapper;
import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemResponseModel;
import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryRequestModel;
import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryResponseModel;
import com.example.backend.shipmentsubdomain.businesslayer.observer.ObserverServiceImpl;
import com.example.backend.shipmentsubdomain.datalayer.observer.Permission;
import com.example.backend.shipmentsubdomain.datalayer.shipment.Shipment;
import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentIdentifier;
import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentRepository;
import com.example.backend.util.exceptions.InvalidInputException;
import com.example.backend.util.exceptions.InvalidOperationException;
import com.example.backend.util.exceptions.InventoryNotFoundException;
import com.example.backend.util.exceptions.ShipmentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{
    private final ShipmentRepository shipmentRepository;
    private final InventoryRequestMapper inventoryRequestMapper;
    private final InventoryResponseMapper inventoryResponseMapper;
    private final InventoryRepository inventoryRepository;
    private final ObserverServiceImpl observerService;

    @Override
    public List<InventoryResponseModel> getInventories(String shipmentId, Optional<String> observerCode) {
        checkObserverPermissions(observerCode, Permission.READ);

        if(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipmentId)==null){
            throw new ShipmentNotFoundException("shipmentId not found: "+shipmentId);
        }

        return inventoryResponseMapper.entitiesToResponseModels(inventoryRepository.findAllByShipmentIdentifier_ShipmentId(shipmentId));
    }

    @Override
    public InventoryResponseModel getInventoryById(String inventoryId, Optional<String> observerCode) {
        checkObserverPermissions(observerCode, Permission.READ);

        Inventory inventory = inventoryRepository.findByInventoryIdentifier_InventoryId(inventoryId);
        if(inventory == null){
            throw new InventoryNotFoundException("Inventory with inventory id: " + inventoryId + " not found");
        } else {
            return inventoryResponseMapper.entityToResponseModel(inventory);
        }
    }

    @Override
    public InventoryResponseModel addInventory(InventoryRequestModel inventoryRequestModel, String shipmentId, Optional<String> observerCode) {
        checkObserverPermissions(observerCode, Permission.EDIT);

        if(!shipmentId.isEmpty() && !shipmentId.isBlank() && inventoryRequestModel.getName() != null && !inventoryRequestModel.getName().isBlank()){
            Shipment shipment = shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipmentId);
            if (shipment != null) {
                Inventory request = inventoryRequestMapper.requestModelToEntity(inventoryRequestModel);
                request.setShipmentIdentifier(new ShipmentIdentifier(shipmentId));
                request.setInventoryIdentifier(new InventoryIdentifier());
                request.setInventoryStatus(InventoryStatus.CREATED);
                return inventoryResponseMapper.entityToResponseModel(inventoryRepository.save(request));
            } else {
                throw new ShipmentNotFoundException("Shipmentid with the id: " + shipmentId + " does not exist.");
            }
        }else {
            throw new InvalidInputException("Request to addInventory to shipmentId: " + shipmentId + " is invalid");
        }
    }

    @Override
    public void deleteInventory(String inventoryId, Optional<String> observerCode) {
        checkObserverPermissions(observerCode, Permission.FULL);

        Inventory inventory = inventoryRepository.findByInventoryIdentifier_InventoryId(inventoryId);
        if (inventory == null) {
            throw new InventoryNotFoundException("Inventory with the id: " + inventoryId + " does not exist.");
        }
        inventoryRepository.deleteById(inventory.getId());
    }

    @Override
    public InventoryResponseModel editInventory(InventoryRequestModel inventoryRequestModel, String inventoryId, Optional<String> observerCode) {
        checkObserverPermissions(observerCode, Permission.EDIT);

        Inventory inventory = inventoryRepository.findByInventoryIdentifier_InventoryId(inventoryId);
        if (inventory == null) {
            throw new InventoryNotFoundException("Inventory with the id: " + inventoryId + " does not exist.");
        }
        inventory.setName(inventoryRequestModel.getName());
        inventory.setDescription(inventoryRequestModel.getDescription());
        inventory.setInventoryStatus(inventoryRequestModel.getInventoryStatus());
        return inventoryResponseMapper.entityToResponseModel(inventoryRepository.save(inventory));
    }

    private void checkObserverPermissions(Optional<String> observerCode, Permission requiredPermission) {
        observerCode.ifPresent(code -> {

            if (code.length() != 6) {
                throw new InvalidInputException("Invalid Observer Code: Observer codes must be 6 characters long.");
            }

            boolean hasPermission = false;
            switch (requiredPermission) {
                case READ:
                    hasPermission = observerService.hasFullPermission(code) ||
                            observerService.hasEditPermission(code) ||
                            observerService.hasReadPermission(code);
                    break;
                case EDIT:
                    hasPermission = observerService.hasEditPermission(code) ||
                            observerService.hasFullPermission(code);
                    break;
                case FULL:
                    hasPermission = observerService.hasFullPermission(code);
                    break;
            }

            if (!hasPermission) {
                throw new InvalidOperationException("Observer lacks " + requiredPermission + " permission");
            }
        });
    }

}
