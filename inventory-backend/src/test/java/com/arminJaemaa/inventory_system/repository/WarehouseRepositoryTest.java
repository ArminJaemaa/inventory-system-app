package com.arminJaemaa.inventory_system.repository;

import com.arminJaemaa.inventory_system.entity.Warehouse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class WarehouseRepositoryTest {

    @Autowired
    private WarehouseRepository warehouseRepository; // Will be RED

    @Test
    void shouldSaveAndFindWarehouseByName() {
        // 1. Setup (Arrange)
        Warehouse warehouse = new Warehouse();
        warehouse.setName("Tallinn Logistics Hub");
        warehouse.setLocation("Tallinn, Estonia");

        // 2. Action (Act)
        Warehouse saved = warehouseRepository.save(warehouse);

        // 3. Verify (Assert)
        assertNotNull(saved.getId()); // Database should generate this
        assertEquals("Tallinn Logistics Hub", saved.getName());
    }
}