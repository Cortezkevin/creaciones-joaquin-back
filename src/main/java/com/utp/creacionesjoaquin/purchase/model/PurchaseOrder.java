package com.utp.creacionesjoaquin.purchase.model;

import com.utp.creacionesjoaquin.commons.enums.PurchaseOrderStatus;
import com.utp.creacionesjoaquin.warehouse.model.RejectionGuide;
import com.utp.creacionesjoaquin.warehouse.model.EntryGuide;
import com.utp.creacionesjoaquin.security.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Timestamp date;

    @Enumerated(EnumType.STRING)
    private PurchaseOrderStatus status;

    private BigDecimal total;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Supplier supplier;

    @OneToOne(mappedBy = "purchaseOrder")
    private PurchaseOrderReception purchaseOrderReception;

    @OneToOne(mappedBy = "purchaseOrder")
    private EntryGuide entryGuide;

    @OneToOne(mappedBy = "purchaseOrder")
    private RejectionGuide rejectionGuide;

    @OneToMany(mappedBy = "purchaseOrder")
    private List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();
}
