package com.utp.creacionesjoaquin.model;

import com.utp.creacionesjoaquin.enums.OrderStatus;
import com.utp.creacionesjoaquin.enums.PaymentMethod;
import com.utp.creacionesjoaquin.enums.ShippingStatus;
import com.utp.creacionesjoaquin.security.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Table(name = "orders")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String note;
    private BigDecimal shippingCost;
    private BigDecimal tax;
    private BigDecimal discount;
    private BigDecimal subtotal;
    private BigDecimal total;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails = new ArrayList<>();
    private String address;
    private LocalDate createdDate;
    private LocalDate cancelledDate;
    private LocalDate completedDate;
    @Enumerated( EnumType.STRING )
    private PaymentMethod paymentMethod;
    @Enumerated( EnumType.STRING )
    private ShippingStatus shippingStatus;
    @Enumerated( EnumType.STRING )
    private OrderStatus status;
    @OneToOne(mappedBy = "order")
    private Invoice invoice;
}
