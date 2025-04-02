package com.utp.creacionesjoaquin.order.model;

import com.utp.creacionesjoaquin.commons.enums.PreparationStatus;
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
public class OrderPreparation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Timestamp createdDate;
    private Timestamp startDate;
    private Timestamp preparedDate;
    private Timestamp completedDate;
    @Enumerated( EnumType.STRING )
    private PreparationStatus status;
    @ManyToOne(fetch = FetchType.EAGER)
    private Grocer grocer;
    @OneToOne(fetch = FetchType.EAGER)
    private Order order;
}
