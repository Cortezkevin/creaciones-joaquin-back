package com.utp.creacionesjoaquin.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private Collection collection;
    @ManyToOne(fetch = FetchType.EAGER)
    private SubCategory subCategory;
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<ProductImages> productImagesList = new ArrayList<>();
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();
    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
