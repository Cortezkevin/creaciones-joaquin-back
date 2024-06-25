package com.utp.creacionesjoaquin.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExitGuide {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Timestamp date;
    private String observations;
    private String warehouseLocation;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    private Order order;

    @OneToOne(fetch = FetchType.EAGER)
    private FabricationProduct fabricationProduct;
}
