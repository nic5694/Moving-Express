package com.example.backend.inventorysubdomain.presentationlayer.inventory;

import com.example.backend.inventorysubdomain.buisnesslayer.inventory.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController()
@RequestMapping(path ="api/v1/movingexpress/shipments/{shipmentId}/inventories")
@CrossOrigin(origins = {"http://localhost:3000","https://movingexpress.systems"}, allowCredentials = "true")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/{inventoryId}")
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")
    public ResponseEntity<InventoryResponseModel> getInventoryById(@PathVariable String inventoryId) {
        return ResponseEntity.ok().body(inventoryService.getInventoryById(inventoryId, Optional.empty()));
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")
    public ResponseEntity<List<InventoryResponseModel>> getInventories(@PathVariable String shipmentId){
        return ResponseEntity.ok().body(inventoryService.getInventories(shipmentId, Optional.empty()));
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")
    public ResponseEntity<InventoryResponseModel> addInventory(@RequestBody InventoryRequestModel inventoryRequestModel,
                                                               @PathVariable String shipmentId){
        return ResponseEntity.status(CREATED)
                .body(inventoryService.addInventory(inventoryRequestModel, shipmentId, Optional.empty()));
    }
    @DeleteMapping("/{inventoryId}")
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")
    public ResponseEntity<Void> deleteInventory(@PathVariable String inventoryId){
        inventoryService.deleteInventory(inventoryId, Optional.empty());
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{inventoryId}")
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")
    public ResponseEntity<InventoryResponseModel> editInventory(@RequestBody InventoryRequestModel inventoryRequestModel,
                                                                @PathVariable String inventoryId){
        return ResponseEntity.status(OK)
                .body(inventoryService.editInventory(inventoryRequestModel, inventoryId, Optional.empty()));
    }
    
}
