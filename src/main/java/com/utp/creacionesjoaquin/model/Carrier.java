package com.utp.creacionesjoaquin.model;

import com.utp.creacionesjoaquin.enums.CarrierStatus;
import com.utp.creacionesjoaquin.security.model.User;
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
public class Carrier {
    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    private String id;
    private String codePlate;
    @Enumerated( EnumType.STRING )
    private CarrierStatus status;
    @OneToOne(fetch = FetchType.EAGER)
    private User user;
    @OneToMany(mappedBy = "carrier")
    private List<OrderShipping> orderShipping = new ArrayList<>();
}
