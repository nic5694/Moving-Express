package com.example.backend.inventorysubdomain.buisnesslayer.Item;

import com.example.backend.inventorysubdomain.datalayer.inventory.Inventory;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryIdentifier;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryRepository;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryStatus;
import com.example.backend.inventorysubdomain.datalayer.Item.Item;
import com.example.backend.inventorysubdomain.datalayer.Item.ItemIdentifier;
import com.example.backend.inventorysubdomain.datalayer.Item.ItemRepository;
import com.example.backend.inventorysubdomain.datalayer.Item.Type;
import com.example.backend.inventorysubdomain.datamapperlayer.Item.ItemRequestMapper;
import com.example.backend.inventorysubdomain.datamapperlayer.Item.ItemResponseMapper;
import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemRequestModel;
import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemResponseModel;
import com.example.backend.shipmentsubdomain.businesslayer.observer.ObserverServiceImpl;
import com.example.backend.shipmentsubdomain.datalayer.Address.Address;
import com.example.backend.shipmentsubdomain.datalayer.Country;
import com.example.backend.shipmentsubdomain.datalayer.shipment.Shipment;
import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentIdentifier;
import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentRepository;
import com.example.backend.shipmentsubdomain.datalayer.shipment.Status;
import com.example.backend.util.exceptions.InvalidInputException;
import com.example.backend.util.exceptions.InvalidOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemServiceImplUnitTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private ShipmentRepository shipmentRepository;
    @Mock
    private ItemResponseMapper itemResponseMapper;
    @Mock
    private ItemRequestMapper itemRequestMapper;
    @InjectMocks
    private ItemServiceImpl itemService;
    @Mock
    private ObserverServiceImpl observerService;

    private Item sampleItem;
    private ItemResponseModel itemResponseModel;
    private ItemRequestModel itemRequestModel;
    private Shipment sampleShipment;
    private Inventory sampleInventory;

    @BeforeEach
    void setUp(){
        sampleShipment=buildShipment();
        sampleInventory=buildInventory(sampleShipment.getShipmentIdentifier());
        sampleItem=buildItem(sampleInventory.getInventoryIdentifier());
        itemRequestModel=buildItemRequestModel();
        itemResponseModel=buildItemResponseModel(sampleInventory.getInventoryIdentifier(), sampleItem.getItemIdentifier());
    }

    @Test
    void addItemWithValidInventoryIdAndShipmentIdAndValidFields_ShouldSucceed(){
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(sampleShipment.getShipmentIdentifier().getShipmentId())).thenReturn(sampleShipment);
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(sampleInventory.getInventoryIdentifier().getInventoryId())).thenReturn(sampleInventory);
        when(itemRequestMapper.requestModelToEntity(itemRequestModel)).thenReturn(sampleItem);
        when(itemRepository.save(sampleItem)).thenReturn(sampleItem);
        when(itemResponseMapper.entityToResponseModel(sampleItem)).thenReturn(itemResponseModel);

        ItemResponseModel result = itemService.addItem(sampleShipment.getShipmentIdentifier().getShipmentId(), sampleInventory.getInventoryIdentifier().getInventoryId(), itemRequestModel,  Optional.empty());
        assertEquals(itemResponseModel, result);
    }

    @Test
    void getAllItemsByInventoryId_ShouldSucceed(){
        Shipment shipment = buildShipment();
        Inventory inventory = buildInventory(shipment.getShipmentIdentifier());
        Item item1=buildItem(inventory.getInventoryIdentifier());
        Item item2=buildItem2(inventory.getInventoryIdentifier());
        List<Item> items=new ArrayList<>();
        items.add(item1);
        items.add(item2);

        ItemResponseModel itemResponseModel1=buildItemResponseModel(inventory.getInventoryIdentifier(), item1.getItemIdentifier());
        ItemResponseModel itemResponseModel2=buildItemResponseModel2(inventory.getInventoryIdentifier(), item2.getItemIdentifier());
        List<ItemResponseModel> itemResponseModels=new ArrayList<>();
        itemResponseModels.add(itemResponseModel1);
        itemResponseModels.add(itemResponseModel2);

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(inventory.getInventoryIdentifier().getInventoryId())).thenReturn(inventory);
        when(itemRepository.findAllByInventoryIdentifier_InventoryId(inventory.getInventoryIdentifier().getInventoryId())).thenReturn(items);
        when(itemResponseMapper.entitiesToResponseModels(items)).thenReturn(itemResponseModels);

        List<ItemResponseModel> result = itemService.getItems(shipment.getShipmentIdentifier().getShipmentId(), inventory.getInventoryIdentifier().getInventoryId(), Optional.empty());
        assertEquals(2, result.size());
        assertEquals(itemResponseModel1, result.get(0));
        assertEquals(itemResponseModel2, result.get(1));
    }

    @Test
    public void getItemByItemId_ShouldSucceed(){
        Shipment shipment = buildShipment();
        Inventory inventory = buildInventory(shipment.getShipmentIdentifier());
        Item item=buildItem(inventory.getInventoryIdentifier());
        ItemResponseModel itemResponseModel=buildItemResponseModel(inventory.getInventoryIdentifier(), item.getItemIdentifier());

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(inventory.getInventoryIdentifier().getInventoryId())).thenReturn(inventory);
        when(itemRepository.findByItemIdentifier_ItemId(item.getItemIdentifier().getItemId())).thenReturn(item);
        when(itemResponseMapper.entityToResponseModel(item)).thenReturn(itemResponseModel);

        ItemResponseModel result=itemService.getItem(shipment.getShipmentIdentifier().getShipmentId(), inventory.getInventoryIdentifier().getInventoryId(), item.getItemIdentifier().getItemId(), Optional.empty());
        assertEquals(itemResponseModel, result);
    }
    // -------------------------------- Observer Related Methods --------------------------------
    @Test
    public void getItemByItemIdWithValidObserverCodePermissionREAD_ShouldSucceed(){
        String validObserverCode = "ABC123";
        when(observerService.hasReadPermission(validObserverCode)).thenReturn(true);

        Shipment shipment = buildShipment();
        Inventory inventory = buildInventory(shipment.getShipmentIdentifier());
        Item item=buildItem(inventory.getInventoryIdentifier());
        ItemResponseModel itemResponseModel=buildItemResponseModel(inventory.getInventoryIdentifier(), item.getItemIdentifier());

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(inventory.getInventoryIdentifier().getInventoryId())).thenReturn(inventory);
        when(itemRepository.findByItemIdentifier_ItemId(item.getItemIdentifier().getItemId())).thenReturn(item);
        when(itemResponseMapper.entityToResponseModel(item)).thenReturn(itemResponseModel);

        ItemResponseModel result=itemService.getItem(shipment.getShipmentIdentifier().getShipmentId(), inventory.getInventoryIdentifier().getInventoryId(), item.getItemIdentifier().getItemId(), Optional.of(validObserverCode));
        assertEquals(itemResponseModel, result);
    }
    @Test
    public void getItemByItemIdWithValidObserverCodePermissionEDIT_ShouldSucceed(){
        String validObserverCode = "ABC123";
        when(observerService.hasEditPermission(validObserverCode)).thenReturn(true);

        Shipment shipment = buildShipment();
        Inventory inventory = buildInventory(shipment.getShipmentIdentifier());
        Item item=buildItem(inventory.getInventoryIdentifier());
        ItemResponseModel itemResponseModel=buildItemResponseModel(inventory.getInventoryIdentifier(), item.getItemIdentifier());

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(inventory.getInventoryIdentifier().getInventoryId())).thenReturn(inventory);
        when(itemRepository.findByItemIdentifier_ItemId(item.getItemIdentifier().getItemId())).thenReturn(item);
        when(itemResponseMapper.entityToResponseModel(item)).thenReturn(itemResponseModel);

        ItemResponseModel result=itemService.getItem(shipment.getShipmentIdentifier().getShipmentId(), inventory.getInventoryIdentifier().getInventoryId(), item.getItemIdentifier().getItemId(), Optional.of(validObserverCode));
        assertEquals(itemResponseModel, result);
    }

    @Test
    public void getItemByItemIdWithValidObserverCodePermissionFULL_ShouldSucceed(){
        String validObserverCode = "ABC123";
        when(observerService.hasFullPermission(validObserverCode)).thenReturn(true);

        Shipment shipment = buildShipment();
        Inventory inventory = buildInventory(shipment.getShipmentIdentifier());
        Item item=buildItem(inventory.getInventoryIdentifier());
        ItemResponseModel itemResponseModel=buildItemResponseModel(inventory.getInventoryIdentifier(), item.getItemIdentifier());

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(inventory.getInventoryIdentifier().getInventoryId())).thenReturn(inventory);
        when(itemRepository.findByItemIdentifier_ItemId(item.getItemIdentifier().getItemId())).thenReturn(item);
        when(itemResponseMapper.entityToResponseModel(item)).thenReturn(itemResponseModel);

        ItemResponseModel result=itemService.getItem(shipment.getShipmentIdentifier().getShipmentId(), inventory.getInventoryIdentifier().getInventoryId(), item.getItemIdentifier().getItemId(), Optional.of(validObserverCode));
        assertEquals(itemResponseModel, result);
    }
    @Test
    void addItemWithValidObserverCodeWithObserverCodePermissionEDIT_ShouldSucceed(){
        String validObserverCode = "ABC123";
        when(observerService.hasEditPermission(validObserverCode)).thenReturn(true);

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(sampleShipment.getShipmentIdentifier().getShipmentId())).thenReturn(sampleShipment);
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(sampleInventory.getInventoryIdentifier().getInventoryId())).thenReturn(sampleInventory);
        when(itemRequestMapper.requestModelToEntity(itemRequestModel)).thenReturn(sampleItem);
        when(itemRepository.save(sampleItem)).thenReturn(sampleItem);
        when(itemResponseMapper.entityToResponseModel(sampleItem)).thenReturn(itemResponseModel);

        ItemResponseModel result = itemService.addItem(sampleShipment.getShipmentIdentifier().getShipmentId(), sampleInventory.getInventoryIdentifier().getInventoryId(), itemRequestModel,Optional.of(validObserverCode));
        assertEquals(itemResponseModel, result);
    }

    @Test
    void addItemWithValidObserverCodeWithObserverCodePermissionFULL_ShouldSucceed(){
        String validObserverCode = "ABC123";
        when(observerService.hasFullPermission(validObserverCode)).thenReturn(true);

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(sampleShipment.getShipmentIdentifier().getShipmentId())).thenReturn(sampleShipment);
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(sampleInventory.getInventoryIdentifier().getInventoryId())).thenReturn(sampleInventory);
        when(itemRequestMapper.requestModelToEntity(itemRequestModel)).thenReturn(sampleItem);
        when(itemRepository.save(sampleItem)).thenReturn(sampleItem);
        when(itemResponseMapper.entityToResponseModel(sampleItem)).thenReturn(itemResponseModel);

        ItemResponseModel result = itemService.addItem(sampleShipment.getShipmentIdentifier().getShipmentId(), sampleInventory.getInventoryIdentifier().getInventoryId(), itemRequestModel,Optional.of(validObserverCode));
        assertEquals(itemResponseModel, result);
    }

    @Test
    void addItemWithValidObserverCodeWithInvalidObserverCodePermission_ShouldFail(){

        String validObserverCode = "ABC12S";
        when(observerService.hasEditPermission(validObserverCode)).thenReturn(false);
        when(observerService.hasReadPermission(validObserverCode)).thenReturn(true);

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(sampleShipment.getShipmentIdentifier().getShipmentId())).thenReturn(sampleShipment);
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(sampleInventory.getInventoryIdentifier().getInventoryId())).thenReturn(sampleInventory);
        when(itemRequestMapper.requestModelToEntity(itemRequestModel)).thenReturn(sampleItem);
        when(itemRepository.save(sampleItem)).thenReturn(sampleItem);
        when(itemResponseMapper.entityToResponseModel(sampleItem)).thenReturn(itemResponseModel);

        ItemResponseModel result = itemService.addItem(sampleShipment.getShipmentIdentifier().getShipmentId(), sampleInventory.getInventoryIdentifier().getInventoryId(), itemRequestModel,  Optional.empty());
        assertEquals(itemResponseModel, result);

        assertThatExceptionOfType(InvalidOperationException.class)
                .isThrownBy(() -> itemService.addItem(sampleShipment.getShipmentIdentifier().getShipmentId(), sampleInventory.getInventoryIdentifier().getInventoryId(), itemRequestModel,  Optional.of(validObserverCode)))
                .withMessageContaining("Observer lacks EDIT permission");
    }
    @Test
    void getItemsWithInvalidObserverCode_ShouldThrowInvalidInputException() {
        // Arrange
        String invalidObserverCode = "ABC12";

        Shipment shipment = buildShipment();
        Inventory inventory = buildInventory(shipment.getShipmentIdentifier());
        Item item1=buildItem(inventory.getInventoryIdentifier());
        Item item2=buildItem2(inventory.getInventoryIdentifier());
        List<Item> items=new ArrayList<>();
        items.add(item1);
        items.add(item2);

        ItemResponseModel itemResponseModel1=buildItemResponseModel(inventory.getInventoryIdentifier(), item1.getItemIdentifier());
        ItemResponseModel itemResponseModel2=buildItemResponseModel2(inventory.getInventoryIdentifier(), item2.getItemIdentifier());
        List<ItemResponseModel> itemResponseModels=new ArrayList<>();
        itemResponseModels.add(itemResponseModel1);
        itemResponseModels.add(itemResponseModel2);

        // Act

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(inventory.getInventoryIdentifier().getInventoryId())).thenReturn(inventory);
        when(itemRepository.findAllByInventoryIdentifier_InventoryId(inventory.getInventoryIdentifier().getInventoryId())).thenReturn(items);
        when(itemResponseMapper.entitiesToResponseModels(items)).thenReturn(itemResponseModels);

        // Assert
        List<ItemResponseModel> result = itemService.getItems(shipment.getShipmentIdentifier().getShipmentId(), inventory.getInventoryIdentifier().getInventoryId(), Optional.empty());
        assertEquals(2, result.size());
        assertEquals(itemResponseModel1, result.get(0));
        assertEquals(itemResponseModel2, result.get(1));
        // Act & Assert
        assertThatExceptionOfType(InvalidInputException.class)
                .isThrownBy(() -> itemService.getItems(shipment.getShipmentIdentifier().getShipmentId(), inventory.getInventoryIdentifier().getInventoryId(), Optional.of(invalidObserverCode)))
                .withMessageContaining("Invalid Observer Code: Observer codes must be 6 characters long.");
    }

    @Test
    void getItemsWithValidObserverCodeButNoReadPermission_ShouldThrowInvalidOperationException() {
        // Arrange
        String validObserverCode = "ABC123";
        when(observerService.hasReadPermission(validObserverCode)).thenReturn(false);

        Shipment shipment = buildShipment();
        Inventory inventory = buildInventory(shipment.getShipmentIdentifier());
        Item item1=buildItem(inventory.getInventoryIdentifier());
        Item item2=buildItem2(inventory.getInventoryIdentifier());
        List<Item> items=new ArrayList<>();
        items.add(item1);
        items.add(item2);

        ItemResponseModel itemResponseModel1=buildItemResponseModel(inventory.getInventoryIdentifier(), item1.getItemIdentifier());
        ItemResponseModel itemResponseModel2=buildItemResponseModel2(inventory.getInventoryIdentifier(), item2.getItemIdentifier());
        List<ItemResponseModel> itemResponseModels=new ArrayList<>();
        itemResponseModels.add(itemResponseModel1);
        itemResponseModels.add(itemResponseModel2);

        // Act

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(inventory.getInventoryIdentifier().getInventoryId())).thenReturn(inventory);
        when(itemRepository.findAllByInventoryIdentifier_InventoryId(inventory.getInventoryIdentifier().getInventoryId())).thenReturn(items);
        when(itemResponseMapper.entitiesToResponseModels(items)).thenReturn(itemResponseModels);

        // Assert
        List<ItemResponseModel> result = itemService.getItems(shipment.getShipmentIdentifier().getShipmentId(), inventory.getInventoryIdentifier().getInventoryId(), Optional.empty());
        assertEquals(2, result.size());
        assertEquals(itemResponseModel1, result.get(0));
        assertEquals(itemResponseModel2, result.get(1));

        assertThatExceptionOfType(InvalidOperationException.class)
                .isThrownBy(() -> itemService.getItems(shipment.getShipmentIdentifier().getShipmentId(), inventory.getInventoryIdentifier().getInventoryId(), Optional.of(validObserverCode)))
                .withMessageContaining("Observer lacks READ permission");
    }

    // -------------------------------- Builder Related Methods --------------------------------
    private Shipment buildShipment() {
        return Shipment.builder()
                .userId("exampleUserId")
                .status(Status.QUOTED)
                .shipmentIdentifier(new ShipmentIdentifier())
                .expectedMovingDate(LocalDate.of(2023, 1, 1))
                .actualMovingDate(LocalDate.of(2023, 1, 5))
                .approximateWeight(100.0)
                .name("Example Shipment")
                .pickupAddress(Address.builder()
                        .city("PickupCity")
                        .streetAddress("PickupStreet")
                        .country(Country.CA)
                        .postalCode("PickupPostalCode")
                        .build())
                .destinationAddress(Address.builder()
                        .city("DestinationCity")
                        .streetAddress("DestinationStreet")
                        .country(Country.USA)
                        .postalCode("DestinationPostalCode")
                        .build())
                .email("example@example.com")
                .phoneNumber("1234567890")
                .build();
    }

    private Inventory buildInventory(ShipmentIdentifier shipmentIdentifier){
        return Inventory.builder()
                .inventoryIdentifier(new InventoryIdentifier())
                .shipmentIdentifier(shipmentIdentifier)
                .name("Do's room")
                .description("This inventory holds the items in Do's room")
                .inventoryStatus(InventoryStatus.CREATED)
                .approximateWeight(200.0)
                .build();
    }

    private Item buildItem(InventoryIdentifier inventoryIdentifier){
        return Item.builder()
                .itemIdentifier(new ItemIdentifier())
                .inventoryIdentifier(inventoryIdentifier)
                .name("La Joconde")
                .type(Type.ITEM)
                .price(BigDecimal.valueOf(100000.0))
                .description("This goes in the kitchen")
                .weight(18.0)
                .handlingInstructions("This should be handled with extreme care!")
                .build();
    }

    private ItemRequestModel buildItemRequestModel(){
        return ItemRequestModel.builder()
                .name("La Joconde")
                .type(Type.ITEM)
                .price(BigDecimal.valueOf(100000.0))
                .description("This goes in the kitchen")
                .weight(18.0)
                .handlingInstructions("This should be handled with extreme care!")
                .build();
    }

    private ItemResponseModel buildItemResponseModel(InventoryIdentifier inventoryIdentifier, ItemIdentifier itemIdentifier){
        return ItemResponseModel.builder()
                .itemId(itemIdentifier.getItemId())
                .inventoryId(inventoryIdentifier.getInventoryId())
                .name("La Joconde")
                .type(Type.ITEM)
                .price(BigDecimal.valueOf(100000.0))
                .description("This goes in the kitchen")
                .weight(18.0)
                .handlingInstructions("This should be handled with extreme care!")
                .build();
    }

    private Item buildItem2(InventoryIdentifier inventoryIdentifier){
        return Item.builder()
                .itemIdentifier(new ItemIdentifier())
                .inventoryIdentifier(inventoryIdentifier)
                .name("The Kiss")
                .type(Type.ITEM)
                .price(BigDecimal.valueOf(100000.0))
                .description("This goes in Do's bedroom")
                .weight(18.0)
                .handlingInstructions("This should be handled with extreme care!")
                .build();
    }

    private ItemResponseModel buildItemResponseModel2(InventoryIdentifier inventoryIdentifier, ItemIdentifier itemIdentifier){
        return ItemResponseModel.builder()
                .itemId(itemIdentifier.getItemId())
                .inventoryId(inventoryIdentifier.getInventoryId())
                .name("The Kiss")
                .type(Type.ITEM)
                .price(BigDecimal.valueOf(100000.0))
                .description("This goes in Do's bedroom")
                .weight(18.0)
                .handlingInstructions("This should be handled with extreme care!")
                .build();
    }

    @Test
    public void deleteItemByItemId_ShouldSucceed(){
        String validObserverCode = "ABC123";
        when(observerService.hasFullPermission(validObserverCode)).thenReturn(true);

        Shipment shipment = buildShipment();
        Inventory inventory = buildInventory(shipment.getShipmentIdentifier());
        Item item=buildItem(inventory.getInventoryIdentifier());

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(inventory.getInventoryIdentifier().getInventoryId())).thenReturn(inventory);
        when(itemRepository.findByItemIdentifier_ItemId(item.getItemIdentifier().getItemId())).thenReturn(item);

        itemService.deleteItem(shipment.getShipmentIdentifier().getShipmentId(), inventory.getInventoryIdentifier().getInventoryId(), item.getItemIdentifier().getItemId(), Optional.of(validObserverCode));

        verify(itemRepository).delete(item);
    }

    @Test
    public void deleteItemWithInvalidObserverCode_ShouldThrowInvalidInputException() {
        // Arrange
        String invalidObserverCode = "ABC12";

        Shipment shipment = buildShipment();
        Inventory inventory = buildInventory(shipment.getShipmentIdentifier());
        Item item=buildItem(inventory.getInventoryIdentifier());

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(inventory.getInventoryIdentifier().getInventoryId())).thenReturn(inventory);
        when(itemRepository.findByItemIdentifier_ItemId(item.getItemIdentifier().getItemId())).thenReturn(item);

        assertThatExceptionOfType(InvalidInputException.class)
                .isThrownBy(() -> itemService.deleteItem(shipment.getShipmentIdentifier().getShipmentId(), inventory.getInventoryIdentifier().getInventoryId(), item.getItemIdentifier().getItemId(), Optional.of(invalidObserverCode)))
                .withMessageContaining("Invalid Observer Code: Observer codes must be 6 characters long.");
    }

    @Test
    void deleteItemWithValidObserverCodeButNoFullPermission_ShouldThrowInvalidOperationException() {
        String validObserverCode = "ABC123";
        when(observerService.hasFullPermission(validObserverCode)).thenReturn(false);

        Shipment shipment = buildShipment();
        Inventory inventory = buildInventory(shipment.getShipmentIdentifier());
        Item item=buildItem(inventory.getInventoryIdentifier());

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(inventory.getInventoryIdentifier().getInventoryId())).thenReturn(inventory);
        when(itemRepository.findByItemIdentifier_ItemId(item.getItemIdentifier().getItemId())).thenReturn(item);

        assertThatExceptionOfType(InvalidOperationException.class)
                .isThrownBy(() -> itemService.deleteItem(shipment.getShipmentIdentifier().getShipmentId(), inventory.getInventoryIdentifier().getInventoryId(), item.getItemIdentifier().getItemId(), Optional.of(validObserverCode)))
                .withMessageContaining("Observer lacks FULL permission");
    }

    @Test
    public void updateItemWithValidFieldsAndEditPermission_ShouldSucceed(){
        String validObserverCode = "ABC123";
        when(observerService.hasEditPermission(validObserverCode)).thenReturn(true);

        Shipment shipment = buildShipment();
        Inventory inventory = buildInventory(shipment.getShipmentIdentifier());
        ItemRequestModel itemRequestModel1=buildItemRequestModel();
        Item item=buildItem(inventory.getInventoryIdentifier());
        ItemResponseModel itemResponseModel1=buildItemResponseModel(inventory.getInventoryIdentifier(), item.getItemIdentifier());

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(inventory.getInventoryIdentifier().getInventoryId())).thenReturn(inventory);
        when(itemRepository.findByItemIdentifier_ItemId(item.getItemIdentifier().getItemId())).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(item);
        when(itemResponseMapper.entityToResponseModel(item)).thenReturn(itemResponseModel1);

        ItemResponseModel result = itemService.updateItem(shipment.getShipmentIdentifier().getShipmentId(), inventory.getInventoryIdentifier().getInventoryId(), item.getItemIdentifier().getItemId(), itemRequestModel1, Optional.of(validObserverCode));
        assertEquals(itemResponseModel1, result);
    }

    @Test
    public void updateItemWithValidFieldsAndFullPermission_ShouldSucceed(){
        String validObserverCode = "ABC123";
        when(observerService.hasFullPermission(validObserverCode)).thenReturn(true);

        Shipment shipment = buildShipment();
        Inventory inventory = buildInventory(shipment.getShipmentIdentifier());
        ItemRequestModel itemRequestModel1=buildItemRequestModel();
        Item item=buildItem(inventory.getInventoryIdentifier());
        ItemResponseModel itemResponseModel1=buildItemResponseModel(inventory.getInventoryIdentifier(), item.getItemIdentifier());

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(inventory.getInventoryIdentifier().getInventoryId())).thenReturn(inventory);
        when(itemRepository.findByItemIdentifier_ItemId(item.getItemIdentifier().getItemId())).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(item);
        when(itemResponseMapper.entityToResponseModel(item)).thenReturn(itemResponseModel1);

        ItemResponseModel result = itemService.updateItem(shipment.getShipmentIdentifier().getShipmentId(), inventory.getInventoryIdentifier().getInventoryId(), item.getItemIdentifier().getItemId(), itemRequestModel1, Optional.of(validObserverCode));
        assertEquals(itemResponseModel1, result);
    }

    @Test
    public void updateItemWithInvalidObserverCode_ShouldThrowInvalidInputException() {
        // Arrange
        String invalidObserverCode = "ABC12";

        Shipment shipment = buildShipment();
        Inventory inventory = buildInventory(shipment.getShipmentIdentifier());
        ItemRequestModel itemRequestModel1=buildItemRequestModel();
        Item item=buildItem(inventory.getInventoryIdentifier());
        ItemResponseModel itemResponseModel1=buildItemResponseModel(inventory.getInventoryIdentifier(), item.getItemIdentifier());

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(inventory.getInventoryIdentifier().getInventoryId())).thenReturn(inventory);
        when(itemRepository.findByItemIdentifier_ItemId(item.getItemIdentifier().getItemId())).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(item);
        when(itemResponseMapper.entityToResponseModel(item)).thenReturn(itemResponseModel1);

        assertThatExceptionOfType(InvalidInputException.class)
                .isThrownBy(() -> itemService.updateItem(shipment.getShipmentIdentifier().getShipmentId(), inventory.getInventoryIdentifier().getInventoryId(), item.getItemIdentifier().getItemId(), itemRequestModel1, Optional.of(invalidObserverCode)))
                .withMessageContaining("Invalid Observer Code: Observer codes must be 6 characters long.");
    }

    @Test
    void updateItemWithValidObserverCodeButNoEditPermission_ShouldThrowInvalidOperationException() {
        String validObserverCode = "ABC123";
        when(observerService.hasEditPermission(validObserverCode)).thenReturn(false);

        Shipment shipment = buildShipment();
        Inventory inventory = buildInventory(shipment.getShipmentIdentifier());
        ItemRequestModel itemRequestModel1=buildItemRequestModel();
        Item item=buildItem(inventory.getInventoryIdentifier());
        ItemResponseModel itemResponseModel1=buildItemResponseModel(inventory.getInventoryIdentifier(), item.getItemIdentifier());

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(inventory.getInventoryIdentifier().getInventoryId())).thenReturn(inventory);
        when(itemRepository.findByItemIdentifier_ItemId(item.getItemIdentifier().getItemId())).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(item);
        when(itemResponseMapper.entityToResponseModel(item)).thenReturn(itemResponseModel1);

        assertThatExceptionOfType(InvalidOperationException.class)
                .isThrownBy(() -> itemService.updateItem(shipment.getShipmentIdentifier().getShipmentId(), inventory.getInventoryIdentifier().getInventoryId(), item.getItemIdentifier().getItemId(), itemRequestModel1, Optional.of(validObserverCode)))
                .withMessageContaining("Observer lacks EDIT permission");
    }
}