package com.utp.creacionesjoaquin.purchase.model;

import com.utp.creacionesjoaquin.commons.enums.PurchaseOrderReceptionStatus;
import com.utp.creacionesjoaquin.warehouse.model.Grocer;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderReception {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String observations;
    private Timestamp createdDate;
    private Timestamp startDate;
    private Timestamp reviewDate;
    private Timestamp completedDate;

    @Enumerated(EnumType.STRING)
    private PurchaseOrderReceptionStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    private Grocer grocer;

    @OneToOne(fetch = FetchType.EAGER)
    private PurchaseOrder purchaseOrder;
}
