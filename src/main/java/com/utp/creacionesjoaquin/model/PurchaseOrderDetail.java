package com.utp.creacionesjoaquin.model;

import com.utp.creacionesjoaquin.enums.PurchaseOrderDetailStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Integer amount;
    private BigDecimal unitPrice;
    private BigDecimal total;

    @Enumerated( EnumType.STRING )
    private PurchaseOrderDetailStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    private RawMaterial rawMaterial;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private PurchaseOrder purchaseOrder;
}
