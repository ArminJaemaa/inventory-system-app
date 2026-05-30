package com.arminJaemaa.inventory_system.repository;

import com.arminJaemaa.inventory_system.entity.Inventory;
import com.arminJaemaa.inventory_system.entity.Product;
import com.arminJaemaa.inventory_system.entity.Warehouse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void findProductFromWarehouse() {
        Warehouse warehouse = Warehouse.builder()
                .name("Tallinn Logistics Hub")
                .location("Tallinn, Estonia")
                .build();
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);

        Product product = Product.builder()
                .name("iphone 16")
                .sku("IPHONE-16")
                .price(new BigDecimal("800.00"))
                .build();

        Product savedProduct = productRepository.save(product);

        Inventory inventory = Inventory.builder()
                .warehouse(savedWarehouse)
                .product(savedProduct)
                .quantity(100)
                .build();

        Inventory savedInventory = inventoryRepository.save(inventory);

        assertNotNull(savedInventory.getId());
        assertEquals(100, savedInventory.getQuantity());
        assertEquals("iphone 16", savedInventory.getProduct().getName());
    }
}
