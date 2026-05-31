package com.arminJaemaa.inventory_system.repository;

import com.arminJaemaa.inventory_system.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByWarehouseIdAndProductId(Long warehouseId, Long productId);
}
