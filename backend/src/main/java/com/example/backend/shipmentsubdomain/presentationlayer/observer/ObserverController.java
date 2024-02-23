package com.example.backend.shipmentsubdomain.presentationlayer.observer;

import com.example.backend.inventorysubdomain.buisnesslayer.inventory.InventoryService;
import com.example.backend.inventorysubdomain.buisnesslayer.Item.ItemService;
import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryRequestModel;
import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryResponseModel;
import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemRequestModel;
import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemResponseModel;
import com.example.backend.shipmentsubdomain.businesslayer.observer.ObserverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController()
@RequestMapping(path ="api/v1/movingexpress/shipments/{shipmentId}/observers")
@CrossOrigin(origins = {"http://localhost:3000","https://movingexpress.systems"}, allowCredentials = "true")
@RequiredArgsConstructor
public class ObserverController {
    private final ObserverService observerService;
    private final InventoryService inventoryService;
    private final ItemService itemService;

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")
    public ResponseEntity<List<ObserverResponseModel>> getAllObservers(@PathVariable String shipmentId){
        return ResponseEntity.ok().body(observerService.getAllObservers(shipmentId));
    }
    @GetMapping("/{observerCode}")
    public ResponseEntity<ObserverResponseModel> getObserverByObserverCode(@PathVariable String observerCode ,
                                                                           @PathVariable String shipmentId){
        return ResponseEntity.ok().body(observerService.getObserverByObserverCode(shipmentId,observerCode));
    }
    @PostMapping()
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")
    public ResponseEntity<ObserverResponseModel> createObserver(@RequestBody ObserverRequestModel observerRequestModel, @PathVariable String shipmentId){
        return ResponseEntity.status(CREATED).body(observerService.createObserver(observerRequestModel, shipmentId));
    }

    @DeleteMapping("/{observerId}")
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")
    public ResponseEntity<Void> deleteObserverCode(@PathVariable String observerId, @PathVariable String shipmentId){
        return ResponseEntity.ok().body(observerService.deleteObserverCode(observerId,shipmentId));
    }
    
    @PutMapping("/{observerId}")
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")
    public ResponseEntity<ObserverResponseModel> editObserverPermission(@RequestBody ObserverRequestModel observerRequestModel,
                                                                        @PathVariable String observerId,
                                                                        @PathVariable String shipmentId){
        return ResponseEntity.ok().body(observerService.editObserverPermission(observerRequestModel, observerId, shipmentId));
    }

    @GetMapping("/inventories/{inventoryId}")
    public ResponseEntity<InventoryResponseModel> getInventoryById(@PathVariable String inventoryId,
                                                                   @RequestParam Optional<String> observerCode) {
        return ResponseEntity.ok().body(inventoryService.getInventoryById(inventoryId, observerCode));
    }

    @GetMapping("/inventories")
    public ResponseEntity<List<InventoryResponseModel>> getInventories(@PathVariable String shipmentId,
                                                                       @RequestParam Optional<String> observerCode){
        return ResponseEntity.ok().body(inventoryService.getInventories(shipmentId, observerCode));
    }

    @PostMapping("/inventories")
    public ResponseEntity<InventoryResponseModel> addInventory(@RequestBody InventoryRequestModel inventoryRequestModel,
                                                               @PathVariable String shipmentId,
                                                               @RequestParam Optional<String> observerCode){
        return ResponseEntity.status(CREATED)
                .body(inventoryService.addInventory(inventoryRequestModel, shipmentId, observerCode));
    }
    @DeleteMapping("/inventories/{inventoryId}")
    public ResponseEntity<Void> deleteInventory(@PathVariable String inventoryId,
                                                @RequestParam Optional<String> observerCode){
        inventoryService.deleteInventory(inventoryId, observerCode);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/inventories/{inventoryId}")
    public ResponseEntity<InventoryResponseModel> editInventory(@RequestBody InventoryRequestModel inventoryRequestModel,
                                                                @PathVariable String inventoryId,
                                                                @RequestParam Optional<String> observerCode){
        return ResponseEntity.status(OK)
                .body(inventoryService.editInventory(inventoryRequestModel, inventoryId, observerCode));
    }
    @PostMapping("/inventories/{inventoryId}/items")
    public ResponseEntity<ItemResponseModel> addItem(@RequestBody ItemRequestModel itemRequestModel, @PathVariable String shipmentId,
                                                     @PathVariable String inventoryId, @RequestParam Optional<String> observerCode){
        return ResponseEntity.status(CREATED).body(itemService.addItem(shipmentId, inventoryId, itemRequestModel, observerCode));
    }

    @GetMapping("/inventories/{inventoryId}/items")
    public ResponseEntity<List<ItemResponseModel>> getItems(@PathVariable String shipmentId, @PathVariable String inventoryId,
                                                            @RequestParam Optional<String> observerCode){
        return ResponseEntity.ok().body(itemService.getItems(shipmentId, inventoryId, observerCode));
    }

    @GetMapping("/inventories/{inventoryId}/items/{itemId}")
    public ResponseEntity<ItemResponseModel> getItem(@PathVariable String shipmentId, @PathVariable String inventoryId,
                                                     @PathVariable String itemId, @RequestParam Optional<String> observerCode){
        return ResponseEntity.ok().body(itemService.getItem(shipmentId, inventoryId, itemId, observerCode));
    }

    @DeleteMapping("/inventories/{inventoryId}/items/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable String shipmentId,
                                           @PathVariable String inventoryId,
                                           @PathVariable String itemId,
                                           @RequestParam Optional<String> observerCode) {
        itemService.deleteItem(shipmentId, inventoryId, itemId, observerCode);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/inventories/{inventoryId}/items/{itemId}")
    public ResponseEntity<ItemResponseModel> updateItem(@PathVariable String shipmentId,
                                                        @PathVariable String inventoryId,
                                                        @PathVariable String itemId,
                                                        @RequestBody ItemRequestModel itemRequestModel,
                                                        @RequestParam Optional<String> observerCode){
        return ResponseEntity.ok().body(itemService.updateItem(shipmentId, inventoryId, itemId, itemRequestModel, observerCode));
    }
}
