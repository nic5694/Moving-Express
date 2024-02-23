package com.example.backend.inventorysubdomain.buisnesslayer.Item;

import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemResponseModel;
import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemRequestModel;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    ItemResponseModel addItem(String shipmentId, String inventoryId, ItemRequestModel itemRequestModel, Optional<String> observerCode);
    List<ItemResponseModel> getItems(String shipmentId, String inventoryId, Optional<String> observerCode);
    ItemResponseModel getItem(String shipmentId, String inventoryId, String itemId, Optional<String> observerCode);
    void deleteItem(String shipmentId, String inventoryId, String itemId, Optional<String> observerCode);
    ItemResponseModel updateItem(String shipmentId, String inventoryId, String itemId, ItemRequestModel itemRequestModel, Optional<String> observerCode);
}