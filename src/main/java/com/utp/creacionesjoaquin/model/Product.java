package com.utp.creacionesjoaquin.model;

import com.utp.creacionesjoaquin.enums.ProductType;
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
    private Integer stock = 0;

    @Enumerated( EnumType.STRING )
    private ProductType type;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private Collection collection;

    @ManyToOne(fetch = FetchType.EAGER)
    private SubCategory subCategory;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<ProductImages> productImagesList = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<InventoryMovements> inventoryMovements = new ArrayList<>();

    @OneToOne(mappedBy = "product")
    private FabricationProduct fabricationProduct;

    @ManyToOne(fetch = FetchType.EAGER)
    private Supplier supplier;
}
