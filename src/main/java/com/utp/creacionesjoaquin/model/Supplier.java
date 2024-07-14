package com.utp.creacionesjoaquin.model;

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
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String ruc;
    private String phone;
    private String address;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private List<PurchaseOrder> purchaseOrders = new ArrayList<>();

    /*@OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private List<EntryGuide> entryGuides = new ArrayList<>();*/

    /*@OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private List<RejectionGuide> rejectionGuides = new ArrayList<>();*/

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private List<Product> productList = new ArrayList<>();

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private List<RawMaterial> rawMaterialList = new ArrayList<>();
}
