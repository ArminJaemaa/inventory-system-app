package com.arminJaemaa.inventory_system.service;

import com.arminJaemaa.inventory_system.entity.Inventory;
import com.arminJaemaa.inventory_system.entity.Product;
import com.arminJaemaa.inventory_system.entity.Warehouse;
import com.arminJaemaa.inventory_system.exception.EntityNotFoundException;
import com.arminJaemaa.inventory_system.repository.InventoryRepository;
import com.arminJaemaa.inventory_system.repository.ProductRepository;
import com.arminJaemaa.inventory_system.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WarehouseInventoryService {

    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    public void addStock(Long warehouseId, Long productId, Integer quantity) {

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Inventory inventory = inventoryRepository.findByWarehouseIdAndProductId(warehouseId, productId)
                .orElseGet(() -> createNewInventory(warehouse,product));

        inventory.addQuantity(quantity);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void removeStock(Long warehouseId, Long productId, Integer quantity) {

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Inventory inventory = inventoryRepository.findByWarehouseIdAndProductId(warehouseId, productId)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found"));

        inventory.subtractQuantity(quantity);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void transferStock(Long sourceWarehouseId, Long destWarehouseId, Long productId, Integer quantity) {
        removeStock(sourceWarehouseId, productId, quantity);
        addStock(destWarehouseId, productId, quantity);
    }

    private Inventory createNewInventory(Warehouse warehouse, Product product) {
        return Inventory.builder()
                .warehouse(warehouse)
                .product(product)
                .quantity(0)
                .build();
    }
}
