package com.utp.creacionesjoaquin.sales.model;

import com.utp.creacionesjoaquin.administration.model.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Integer amount;
    private BigDecimal total;
    @ManyToOne(fetch = FetchType.EAGER)
    private Cart cart;
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;
}
