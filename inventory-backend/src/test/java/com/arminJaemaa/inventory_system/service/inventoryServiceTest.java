package com.arminJaemaa.inventory_system.service;

import com.arminJaemaa.inventory_system.entity.Inventory;
import com.arminJaemaa.inventory_system.entity.Product;
import com.arminJaemaa.inventory_system.entity.Warehouse;
import com.arminJaemaa.inventory_system.repository.InventoryRepository;
import com.arminJaemaa.inventory_system.repository.ProductRepository;
import com.arminJaemaa.inventory_system.repository.WarehouseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class inventoryServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private WarehouseInventoryService warehouseInventoryService;

    @Test
    void addStockTest() {
        Long warehouseId = 1L;
        Long productId = 2L;

        Warehouse mockWarehouse = Warehouse.builder()
                .id(warehouseId)
                .name("Tallinn Main Hub")
                .build();

        Product mockProduct = Product.builder()
                .id(productId)
                .name("iphone 16")
                .sku("IPHONE-16")
                .build();

        Inventory mockExistingInventory = Inventory.builder()
                .warehouse(mockWarehouse)
                .product(mockProduct)
                .quantity(100)
                .build();

        when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(mockWarehouse));
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        when(inventoryRepository.findByWarehouseIdAndProductId(warehouseId, productId))
                .thenReturn(Optional.of(mockExistingInventory));

        warehouseInventoryService.addStock(warehouseId, productId, 50);

        arrertEquals(150, mockExistingInventory.getQuantity());
        verify(inventoryRepository).save(mockExistingInventory);
    }
}
