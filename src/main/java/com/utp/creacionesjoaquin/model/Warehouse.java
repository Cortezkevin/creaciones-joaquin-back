package com.utp.creacionesjoaquin.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String location;

    @OneToMany(mappedBy = "warehouse", fetch = FetchType.LAZY)
    List<InventoryMovements> inventoryMovements = new ArrayList<>();
}
