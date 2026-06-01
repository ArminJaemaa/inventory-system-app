package com.arminJaemaa.inventory_system.service;

import com.arminJaemaa.inventory_system.entity.Inventory;
import com.arminJaemaa.inventory_system.entity.Product;
import com.arminJaemaa.inventory_system.entity.Warehouse;
import com.arminJaemaa.inventory_system.exception.EntityNotFoundException;
import com.arminJaemaa.inventory_system.repository.InventoryRepository;
import com.arminJaemaa.inventory_system.repository.ProductRepository;
import com.arminJaemaa.inventory_system.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehouseInventoryService {

    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    private void performAddStock(Long warehouseId, Long productId, Integer quantity) {

        Warehouse warehouse = findWarehouseByWarehouseId(warehouseId);
        Product product = findProductByProductId(productId);

        Inventory inventory = inventoryRepository.findByWarehouseIdAndProductId(warehouseId, productId)
                .orElseGet(() -> createNewInventory(warehouse,product));

        inventory.addQuantity(quantity);
        inventoryRepository.save(inventory);
    }

    private void performRemoveStock(Long warehouseId, Long productId, Integer quantity) {

        Warehouse warehouse = findWarehouseByWarehouseId(warehouseId);
        Product product = findProductByProductId(productId);

        Inventory inventory = inventoryRepository.findByWarehouseIdAndProductId(warehouseId, productId)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found"));

        inventory.subtractQuantity(quantity);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void addStock(Long warehouseId, Long productId, Integer quantity) {
        performAddStock(warehouseId, productId, quantity);
    }

    @Transactional
    public void removeStock(Long warehouseId, Long productId, Integer quantity) {
        performRemoveStock(warehouseId, productId, quantity);
    }

    @Transactional
    public void transferStock(Long sourceWarehouseId, Long destWarehouseId, Long productId, Integer quantity) {
        performRemoveStock(sourceWarehouseId, productId, quantity);
        performAddStock(destWarehouseId, productId, quantity);
    }

    private Inventory createNewInventory(Warehouse warehouse, Product product) {
        return Inventory.builder()
                .warehouse(warehouse)
                .product(product)
                .quantity(0)
                .build();
    }

    private Warehouse findWarehouseByWarehouseId(Long warehouseId) {
        return findOrThrow(
                warehouseRepository.findById(warehouseId),
                "Warehouse not found"
        );
    }

    private Product findProductByProductId(Long productId) {
        return findOrThrow(
                productRepository.findById(productId),
                "Product not found"
        );
    }

    private <T> T findOrThrow(Optional<T> optional, String errorMessage) {
        return optional.orElseThrow(() -> new EntityNotFoundException(errorMessage));
    }
}
