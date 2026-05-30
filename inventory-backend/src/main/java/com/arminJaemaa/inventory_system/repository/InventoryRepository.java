package com.arminJaemaa.inventory_system.repository;

import com.arminJaemaa.inventory_system.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
