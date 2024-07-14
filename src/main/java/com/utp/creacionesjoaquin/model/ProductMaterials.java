package com.utp.creacionesjoaquin.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "product_materials")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductMaterials {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    private RawMaterial rawMaterial;

    private int amountNeeded;
}
