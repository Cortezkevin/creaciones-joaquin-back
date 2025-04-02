package com.utp.creacionesjoaquin.administration.model;

import com.utp.creacionesjoaquin.commons.enums.ProductType;
import com.utp.creacionesjoaquin.sales.model.CartItem;
import com.utp.creacionesjoaquin.order.model.OrderDetail;
import com.utp.creacionesjoaquin.purchase.model.PurchaseOrderDetail;
import com.utp.creacionesjoaquin.purchase.model.Supplier;
import com.utp.creacionesjoaquin.warehouse.model.InventoryMovements;
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

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<ProductMaterials> productRawMaterials = new ArrayList<>();

}
