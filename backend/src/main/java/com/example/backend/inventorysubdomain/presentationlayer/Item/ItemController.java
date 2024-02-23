package com.example.backend.inventorysubdomain.presentationlayer.Item;

import com.example.backend.inventorysubdomain.buisnesslayer.Item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;

@RestController()
@RequestMapping(path ="api/v1/movingexpress/shipments/{shipmentId}/inventories/{inventoryId}/items")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")
    public ResponseEntity<ItemResponseModel> addItem(@RequestBody ItemRequestModel itemRequestModel, @PathVariable String shipmentId,
                                                     @PathVariable String inventoryId){
        return ResponseEntity.status(CREATED).body(itemService.addItem(shipmentId, inventoryId, itemRequestModel, Optional.empty()));
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")

    public ResponseEntity<List<ItemResponseModel>> getItems(@PathVariable String shipmentId, @PathVariable String inventoryId){
        return ResponseEntity.ok().body(itemService.getItems(shipmentId, inventoryId, Optional.empty()));
    }

    @GetMapping("/{itemId}")
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")
    public ResponseEntity<ItemResponseModel> getItem(@PathVariable String shipmentId, @PathVariable String inventoryId,
                                                     @PathVariable String itemId){
        return ResponseEntity.ok().body(itemService.getItem(shipmentId, inventoryId, itemId, Optional.empty()));
    }

    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")
    public ResponseEntity<Void> deleteItem(@PathVariable String shipmentId,
                                           @PathVariable String inventoryId,
                                           @PathVariable String itemId) {
        itemService.deleteItem(shipmentId, inventoryId, itemId, Optional.empty());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{itemId}")
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")
    public ResponseEntity<ItemResponseModel> updateItem(@PathVariable String shipmentId,
                                                        @PathVariable String inventoryId,
                                                        @PathVariable String itemId,
                                                        @RequestBody ItemRequestModel itemRequestModel){
        return ResponseEntity.ok().body(itemService.updateItem(shipmentId, inventoryId, itemId, itemRequestModel, Optional.empty()));
    }
}