package com.utp.creacionesjoaquin.model;

import com.utp.creacionesjoaquin.enums.PreparationStatus;
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
