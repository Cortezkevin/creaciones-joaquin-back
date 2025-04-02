package com.utp.creacionesjoaquin.order.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    private String id;
    private LocalDate issuedDate;
    private String url;
    @OneToOne(fetch = FetchType.EAGER)
    private Order order;
}
