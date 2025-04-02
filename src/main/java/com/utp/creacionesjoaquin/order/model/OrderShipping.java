package com.utp.creacionesjoaquin.order.model;

import com.utp.creacionesjoaquin.commons.enums.ShippingStatus;
import com.utp.creacionesjoaquin.warehouse.model.Carrier;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderShipping {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String address;
    private String preparedBy;
    private Double distance = 0.0;
    private Timestamp createdDate;
    private Timestamp startDate;
    private Timestamp preparedDate;
    private Timestamp shippingDate;
    private Timestamp completedDate;
    @ManyToOne(fetch = FetchType.EAGER)
    private Carrier carrier;
    @Enumerated( EnumType.STRING )
    private ShippingStatus status;
    @OneToOne(fetch = FetchType.EAGER)
    private Order order;
}
