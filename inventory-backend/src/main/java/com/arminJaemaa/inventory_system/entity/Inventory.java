package com.arminJaemaa.inventory_system.entity;

import com.arminJaemaa.inventory_system.exception.InsufficientStockException;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    public void addQuantity(Integer amount) {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        if (this.quantity == null) { //TODO: dead code. can remove safely
            this.quantity = 0;
        }
        this.quantity += amount;
    }

    public void subtractQuantity(Integer amount) {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        if (this.quantity < amount) {
            throw new InsufficientStockException("Inventory only has " + this.quantity + "available");
        }

        this.quantity -= amount;
    }
}
