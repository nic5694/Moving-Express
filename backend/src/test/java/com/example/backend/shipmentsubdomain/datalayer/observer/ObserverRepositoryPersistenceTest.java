package com.example.backend.shipmentsubdomain.datalayer.observer;

import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ObserverRepositoryPersistenceTest {

    @Autowired
    private ObserverRepository observerRepository;

    @BeforeEach
    public void setUp() {
        observerRepository.deleteAll();
    }

    @Test
    void whenFindObserverByObserverCode_thenReturnObserver() {
        // given
        Observer observer = new Observer();
        observer.setObserverCode("OBS001");
        observerRepository.save(observer);

        // when
        Observer observer1 = observerRepository.findObserverByObserverCode("OBS001");

        // then
        assertNotNull(observer1);
        assertEquals("OBS001", observer1.getObserverCode());
    }

    @Test
    void whenExistsByObserverCode_thenReturnTrue() {
        // given
        Observer observer = new Observer();
        observer.setObserverCode("OBS002");
        observerRepository.save(observer);

        // when
        boolean exists = observerRepository.existsByObserverCode("OBS002");

        // then
        assertTrue(exists);
    }

    @Test
    void whenFindAllByShipmentIdentifier_ShipmentId_thenReturnObserversList() {
        // given
        String shipmentId = "SHIP001";
        Observer observer1 = new Observer();
        observer1.setObserverCode("OBS003");
        observer1.setShipmentIdentifier(new ShipmentIdentifier(shipmentId));
        observerRepository.save(observer1);

        Observer observer2 = new Observer();
        observer2.setObserverCode("OBS004");
        observer2.setShipmentIdentifier(new ShipmentIdentifier(shipmentId));
        observerRepository.save(observer2);

        // when
        List<Observer> foundObservers = observerRepository.findAllByShipmentIdentifier_ShipmentId(shipmentId);

        // then
        assertNotNull(foundObservers);
        assertEquals(2, foundObservers.size());
        assertTrue(foundObservers.stream().anyMatch(o -> o.getObserverCode().equals("OBS003")));
        assertTrue(foundObservers.stream().anyMatch(o -> o.getObserverCode().equals("OBS004")));
    }
    @Test
    void whenFindByObserverIdentifier_ObserverId_thenReturnObserver() {
        // given
        String observerId = "DummyObserverId";
        Observer observer = new Observer();
        observer.setObserverIdentifier(new ObserverIdentifier(observerId));
        observerRepository.save(observer);

        // when
        Observer foundObserver = observerRepository.findByObserverIdentifier_ObserverId(observerId);

        // then
        assertNotNull(foundObserver);
        assertEquals(observerId, foundObserver.getObserverIdentifier().getObserverId());
    }

    @Test
    void whenFindByObserverIdentifier_ObserverId_WithNonExistentId_thenReturnNull() {
        // given
        String nonExistentObserverId = "NON_EXISTENT";

        // when
        Observer foundObserver = observerRepository.findByObserverIdentifier_ObserverId(nonExistentObserverId);

        // then
        assertNull(foundObserver);
    }

}
