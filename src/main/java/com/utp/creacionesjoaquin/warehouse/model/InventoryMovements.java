package com.utp.creacionesjoaquin.warehouse.model;

import com.utp.creacionesjoaquin.commons.enums.InventoryMovementType;
import com.utp.creacionesjoaquin.administration.model.Product;
import com.utp.creacionesjoaquin.purchase.model.RawMaterial;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryMovements {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private InventoryMovementType type;

    private Integer initialStock;
    private Integer amount;
    private Integer newStock;
    private Timestamp date;
    private String reason;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    private RawMaterial rawMaterial;

    @ManyToOne(fetch = FetchType.EAGER)
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.EAGER)
    private EntryGuide entryGuide;

    @ManyToOne(fetch = FetchType.EAGER)
    private ExitGuide exitGuide;
}
