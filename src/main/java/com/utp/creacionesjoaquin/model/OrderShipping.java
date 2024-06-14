package com.utp.creacionesjoaquin.model;

import com.utp.creacionesjoaquin.enums.ShippingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
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
