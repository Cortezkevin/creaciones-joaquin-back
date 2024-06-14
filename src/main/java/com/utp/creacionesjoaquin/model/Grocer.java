package com.utp.creacionesjoaquin.model;

import com.utp.creacionesjoaquin.enums.GrocerStatus;
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
public class Grocer {
    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    private String id;
    @Enumerated( EnumType.STRING )
    private GrocerStatus status;
    @OneToOne(fetch = FetchType.EAGER)
    private User user;
    @OneToMany(mappedBy = "grocer")
    private List<OrderPreparation> orderPreparations = new ArrayList<>();
}
