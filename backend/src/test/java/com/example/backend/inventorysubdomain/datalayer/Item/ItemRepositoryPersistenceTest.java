package com.example.backend.inventorysubdomain.datalayer.Item;

import com.example.backend.inventorysubdomain.datalayer.inventory.Inventory;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryIdentifier;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryRepository;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryStatus;
import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemResponseModel;
import com.example.backend.shipmentsubdomain.datalayer.Address.Address;
import com.example.backend.shipmentsubdomain.datalayer.Country;
import com.example.backend.shipmentsubdomain.datalayer.shipment.Shipment;
import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentIdentifier;
import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentRepository;
import com.example.backend.shipmentsubdomain.datalayer.shipment.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
class ItemRepositoryPersistenceTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ShipmentRepository shipmentRepository;

    private Shipment shipment;
    private Inventory inventory;
    private Item item;
    private Item item2;

    @BeforeEach
    public void setUp(){
        itemRepository.deleteAll();

        shipment=buildShipment();
        inventory=buildInventory(shipment.getShipmentIdentifier());
        item=buildItem(inventory.getInventoryIdentifier());

        itemRepository.save(item);

        item2=new Item("Spoon", Type.BOX, BigDecimal.valueOf(9.0), "This box goes in the kitchen", 10.0, "It doesn't need to be handled in a specific manner", inventory.getInventoryIdentifier());
        itemRepository.save(item2);
    }

    @AfterEach
    public void tearDown(){
        itemRepository.deleteAll();
    }

    @Test
    public void whenSaveItemWithValues_thenValuesAreCorrectlyPersisted(){
        Item savedItem=itemRepository.save(item);

        assertEquals(item, savedItem);
    }

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
                .inventoryStatus(InventoryStatus.IN_PROGRESS)
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
    public void whenFindAllByInventoryId_thenReturnAllItems(){
        int expectedItemNumber=2;

        List<Item> items = itemRepository.findAllByInventoryIdentifier_InventoryId(inventory.getInventoryIdentifier().getInventoryId());
        assertEquals(expectedItemNumber, items.size());
        assertEquals(item, items.get(0));
        assertEquals(item2, items.get(1));
    }

    @Test
    public void whenFindByItemId_thenReturnItem(){
        Item existingItem=itemRepository.findByItemIdentifier_ItemId(item.getItemIdentifier().getItemId());

        assertEquals(existingItem, item);
    }

    @Test
    public void whenDeleteItemByItemId_thenReturnVoid(){
        itemRepository.delete(item);

        assertNull(itemRepository.findByItemIdentifier_ItemId(item.getItemIdentifier().getItemId()));
    }
}