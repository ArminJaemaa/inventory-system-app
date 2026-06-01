package com.arminJaemaa.inventory_system.service;

import com.arminJaemaa.inventory_system.entity.Inventory;
import com.arminJaemaa.inventory_system.entity.Product;
import com.arminJaemaa.inventory_system.entity.Warehouse;
import com.arminJaemaa.inventory_system.exception.InsufficientStockException;
import com.arminJaemaa.inventory_system.repository.InventoryRepository;
import com.arminJaemaa.inventory_system.repository.ProductRepository;
import com.arminJaemaa.inventory_system.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private WarehouseInventoryService warehouseInventoryService;

    private Warehouse mockWarehouse;
    private Product mockProduct;

    Long warehouseId = 1L;
    Long productId = 2L;

    @BeforeEach
    void setUp() {
        mockWarehouse = Warehouse.builder().id(warehouseId).name("Hub").build();
        mockProduct = Product.builder().id(productId).sku("IPHONE").build();
    }

    @Test
    void addStockTest() {

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

        assertEquals(150, mockExistingInventory.getQuantity());
        verify(inventoryRepository).save(mockExistingInventory);
    }

    @Test
    void removeStockTest_shouldRemoveStockGivenAmount() {

        Inventory mockExistingInventory = Inventory.builder()
                .warehouse(mockWarehouse)
                .product(mockProduct)
                .quantity(100)
                .build();

        when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(mockWarehouse));
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        when(inventoryRepository.findByWarehouseIdAndProductId(warehouseId, productId))
                .thenReturn(Optional.of(mockExistingInventory));

        warehouseInventoryService.removeStock(warehouseId,productId, 50);
        assertEquals(50, mockExistingInventory.getQuantity());
        verify(inventoryRepository).save(mockExistingInventory);
    }

    @Test
    void removeStockTest_shouldNotRemoveStockGivenAmount() {

        Inventory mockExistingInventory = Inventory.builder()
                .warehouse(mockWarehouse)
                .product(mockProduct)
                .quantity(20)
                .build();

        when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(mockWarehouse));
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(inventoryRepository.findByWarehouseIdAndProductId(warehouseId, productId))
                .thenReturn(Optional.of(mockExistingInventory));

        assertThrows(InsufficientStockException.class, ()-> warehouseInventoryService.removeStock(warehouseId, productId, 50));
    }

    @Test
    void transferStockTest_shouldTransferStockGivenAmountToDifferentWarehouse() {

        Long destWarehouseId = 10L;
        Integer transferQuantity = 50;

        Warehouse destinationWarehouse = Warehouse.builder().id(destWarehouseId).name("destination-warehouse").build();

        Inventory mockExistingSourceInventory = Inventory.builder()
                .warehouse(mockWarehouse)
                .product(mockProduct)
                .quantity(100)
                .build();

        Inventory mockDestinationInventory = Inventory.builder()
                .warehouse(destinationWarehouse)
                .product(mockProduct)
                .quantity(50)
                .build();

        when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(mockWarehouse));
        when(warehouseRepository.findById(destWarehouseId)).thenReturn(Optional.of(destinationWarehouse));
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        when(inventoryRepository.findByWarehouseIdAndProductId(warehouseId, productId))
                .thenReturn(Optional.of(mockExistingSourceInventory));
        when(inventoryRepository.findByWarehouseIdAndProductId(destWarehouseId, productId))
                .thenReturn(Optional.of(mockDestinationInventory));

        warehouseInventoryService.transferStock(warehouseId, destWarehouseId, productId, transferQuantity);

        assertEquals(50, mockExistingSourceInventory.getQuantity());
        assertEquals(100, mockDestinationInventory.getQuantity());

        verify(inventoryRepository).save(mockExistingSourceInventory);
        verify(inventoryRepository).save(mockDestinationInventory);
    }
}
