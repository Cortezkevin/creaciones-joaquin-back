package com.utp.creacionesjoaquin.warehouse.model;

import com.utp.creacionesjoaquin.commons.enums.GrocerStatus;
import com.utp.creacionesjoaquin.order.model.OrderPreparation;
import com.utp.creacionesjoaquin.purchase.model.PurchaseOrderReception;
import com.utp.creacionesjoaquin.security.model.User;
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
public class Grocer {
    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    private String id;

    @Enumerated( EnumType.STRING )
    private GrocerStatus status;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    @OneToMany(mappedBy = "grocer", fetch = FetchType.LAZY)
    private List<OrderPreparation> orderPreparations = new ArrayList<>();

    @OneToMany(mappedBy = "grocer", fetch = FetchType.LAZY)
    private List<PurchaseOrderReception> purchaseOrderReceptions = new ArrayList<>();
}
