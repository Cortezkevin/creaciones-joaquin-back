package com.utp.creacionesjoaquin.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.utp.creacionesjoaquin.sales.model.Cart;
import com.utp.creacionesjoaquin.order.model.Order;
import com.utp.creacionesjoaquin.profile.model.PersonalInformation;
import com.utp.creacionesjoaquin.purchase.model.PurchaseOrder;
import com.utp.creacionesjoaquin.warehouse.model.Carrier;
import com.utp.creacionesjoaquin.warehouse.model.Grocer;
import com.utp.creacionesjoaquin.security.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID )
    private String id;
    private String email;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String tokenPassword;

    @Enumerated( EnumType.STRING )
    private Status status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn( name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn( name = "role_id", nullable = false)
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user")
    private Cart cart;

    @OneToOne(mappedBy = "user")
    private PersonalInformation personalInformation;

    @OneToOne(mappedBy = "user")
    private Carrier carrier;

    @OneToOne(mappedBy = "user")
    private Grocer grocer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<PurchaseOrder> purchaseOrders = new ArrayList<>();
}
