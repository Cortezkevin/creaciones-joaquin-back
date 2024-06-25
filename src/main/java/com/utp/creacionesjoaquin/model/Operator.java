package com.utp.creacionesjoaquin.model;


import com.utp.creacionesjoaquin.enums.GrocerStatus;
import com.utp.creacionesjoaquin.security.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Operator {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated( EnumType.STRING )
    private GrocerStatus status;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    /*@ManyToMany(mappedBy = "operators")
    private List<FabricationProduct> fabricationProducts = new ArrayList<>();*/
}
