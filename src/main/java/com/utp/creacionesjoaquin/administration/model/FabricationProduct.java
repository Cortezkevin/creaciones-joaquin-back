package com.utp.creacionesjoaquin.administration.model;

import com.utp.creacionesjoaquin.commons.enums.FabricationStatus;
import com.utp.creacionesjoaquin.warehouse.model.ExitGuide;
import com.utp.creacionesjoaquin.warehouse.model.Operator;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FabricationProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Timestamp createdDate;
    private Timestamp startDate;
    private Timestamp endDate;
    private String serieNumber;
    private Integer amount;

    @Enumerated(EnumType.STRING)
    private FabricationStatus status;

    private String observations;

    @OneToOne(fetch = FetchType.EAGER)
    private Product product;

    @OneToOne(mappedBy = "fabricationProduct")
    private ExitGuide exitGuide;

    //QUITAR RELACION PORQUE LA ENTIDAD PRODUCTOS YA CUENTA CON UNA RELACION CON LOS MATERIALES
    /*@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "fabrication_materials",
            joinColumns = @JoinColumn( name = "fabrication_product_id", nullable = false),
            inverseJoinColumns = @JoinColumn( name = "raw_material_id", nullable = false)
    )
    private List<RawMaterial> rawMaterials = new ArrayList<>();*/

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "fabrication_operators",
            joinColumns = @JoinColumn( name = "fabrication_product_id", nullable = false),
            inverseJoinColumns = @JoinColumn( name = "operator_id", nullable = false)
    )
    private List<Operator> operators = new ArrayList<>();
}
