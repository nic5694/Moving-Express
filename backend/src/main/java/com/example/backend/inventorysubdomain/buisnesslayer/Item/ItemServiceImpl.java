package com.example.backend.inventorysubdomain.buisnesslayer.Item;

import com.example.backend.inventorysubdomain.datalayer.Item.Item;
import com.example.backend.inventorysubdomain.datalayer.Item.ItemIdentifier;
import com.example.backend.inventorysubdomain.datalayer.Item.ItemRepository;
import com.example.backend.inventorysubdomain.datalayer.Item.Type;
import com.example.backend.inventorysubdomain.datalayer.inventory.Inventory;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryIdentifier;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryRepository;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryStatus;
import com.example.backend.inventorysubdomain.datamapperlayer.Item.ItemRequestMapper;
import com.example.backend.inventorysubdomain.datamapperlayer.Item.ItemResponseMapper;
import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemRequestModel;
import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemResponseModel;
import com.example.backend.shipmentsubdomain.businesslayer.observer.ObserverServiceImpl;
import com.example.backend.shipmentsubdomain.datalayer.observer.Permission;
import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentRepository;
import com.example.backend.util.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ShipmentRepository shipmentRepository;
    private final InventoryRepository inventoryRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemResponseMapper itemResponseMapper;
    private final ObserverServiceImpl observerService;

    @Override
    public ItemResponseModel addItem(String shipmentId, String inventoryId, ItemRequestModel itemRequestModel, Optional<String> observerCode) {
        checkObserverPermissions(observerCode, Permission.EDIT);

        if(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipmentId)==null){
            throw new ShipmentNotFoundException("shipmentId not found: "+shipmentId);
        }

        Inventory existingInventory=inventoryRepository.findByInventoryIdentifier_InventoryId(inventoryId);
        if(existingInventory==null){
            throw new InventoryNotFoundException("inventoryId not found: "+inventoryId);
        }

        if(existingInventory.getInventoryStatus()==InventoryStatus.PACKED||existingInventory.getInventoryStatus()== InventoryStatus.IN_PROGRESS){
            throw new InvalidInventoryStatusException("invalid inventoryStatus: "+existingInventory.getInventoryStatus());
        }

        if(itemRequestModel.getType()!= Type.ITEM && itemRequestModel.getType()!= Type.BOX){
            throw new InvalidTypeException("invalid type: "+itemRequestModel.getType());
        }

        Item newItem=itemRequestMapper.requestModelToEntity(itemRequestModel);
        newItem.setItemIdentifier(new ItemIdentifier());
        newItem.setInventoryIdentifier(new InventoryIdentifier(inventoryId));

        return itemResponseMapper.entityToResponseModel(itemRepository.save(newItem));
    }

    @Override
    public List<ItemResponseModel> getItems(String shipmentId, String inventoryId, Optional<String> observerCode) {
        checkObserverPermissions(observerCode, Permission.READ);

        if(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipmentId)==null){
            throw new ShipmentNotFoundException("shipmentId not found: "+shipmentId);
        }

        if(inventoryRepository.findByInventoryIdentifier_InventoryId(inventoryId)==null){
            throw new InventoryNotFoundException("inventoryId not found: "+inventoryId);
        }

        return itemResponseMapper.entitiesToResponseModels(itemRepository.findAllByInventoryIdentifier_InventoryId(inventoryId));
    }

    @Override
    public ItemResponseModel getItem(String shipmentId, String inventoryId, String itemId, Optional<String> observerCode) {
        checkObserverPermissions(observerCode, Permission.READ);

        if(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipmentId)==null){
            throw new ShipmentNotFoundException("shipmentId not found: "+shipmentId);
        }

        if(inventoryRepository.findByInventoryIdentifier_InventoryId(inventoryId)==null){
            throw new InventoryNotFoundException("inventoryId not found: "+inventoryId);
        }

        Item existingItem=itemRepository.findByItemIdentifier_ItemId(itemId);
        if(existingItem==null){
            throw new ItemNotFoundException("itemId not found: "+itemId);
        }

        return itemResponseMapper.entityToResponseModel(existingItem);
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

    @Override
    public void deleteItem(String shipmentId, String inventoryId, String itemId, Optional<String> observerCode) {
        checkObserverPermissions(observerCode, Permission.FULL);

        if(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipmentId)==null){
            throw new ShipmentNotFoundException("shipmentId not found: "+shipmentId);
        }

        if(inventoryRepository.findByInventoryIdentifier_InventoryId(inventoryId)==null){
            throw new InventoryNotFoundException("inventoryId not found: "+inventoryId);
        }

        Item existingItem=itemRepository.findByItemIdentifier_ItemId(itemId);
        if(existingItem==null){
            throw new ItemNotFoundException("itemId not found: "+itemId);
        }

        itemRepository.delete(existingItem);
    }

    @Override
    public ItemResponseModel updateItem(String shipmentId, String inventoryId, String itemId, ItemRequestModel itemRequestModel, Optional<String> observerCode) {
        checkObserverPermissions(observerCode, Permission.EDIT);

        if(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipmentId)==null){
            throw new ShipmentNotFoundException("shipmentId not found: "+shipmentId);
        }

        if(inventoryRepository.findByInventoryIdentifier_InventoryId(inventoryId)==null){
            throw new InventoryNotFoundException("inventoryId not found: "+inventoryId);
        }

        Item existingItem=itemRepository.findByItemIdentifier_ItemId(itemId);
        if(existingItem==null){
            throw new ItemNotFoundException("itemId not found: "+itemId);
        }

        existingItem.setName(itemRequestModel.getName());
        existingItem.setType(itemRequestModel.getType());
        existingItem.setPrice(itemRequestModel.getPrice());
        existingItem.setDescription(itemRequestModel.getDescription());
        existingItem.setHandlingInstructions(itemRequestModel.getHandlingInstructions());
        existingItem.setWeight(itemRequestModel.getWeight());

        return itemResponseMapper.entityToResponseModel(itemRepository.save(existingItem));
    }
}