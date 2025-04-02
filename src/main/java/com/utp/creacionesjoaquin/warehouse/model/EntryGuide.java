package com.utp.creacionesjoaquin.warehouse.model;

import com.utp.creacionesjoaquin.purchase.model.PurchaseOrder;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntryGuide {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Timestamp date;
    private String productConditions;

    @ManyToOne(fetch = FetchType.EAGER)
    private Grocer grocer;

    //quitar
    /*ManyToOne(fetch = FetchType.EAGER)
    private Supplier supplier;*/

    @OneToOne(fetch = FetchType.EAGER)
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    private Warehouse warehouse;

    @OneToMany(mappedBy = "entryGuide")
    private List<InventoryMovements> inventoryMovementsList = new ArrayList<>();

}
