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
public class RejectionGuide {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Timestamp date;
    private String reason;
    private String productConditions;
    private String suggestions;

    @ManyToOne(fetch = FetchType.EAGER)
    private Grocer grocer;

    /*@ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Supplier supplier;*/

    @OneToOne(fetch = FetchType.EAGER)
    private PurchaseOrder purchaseOrder;
}
