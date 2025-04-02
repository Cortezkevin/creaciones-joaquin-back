package com.utp.creacionesjoaquin.administration.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "product_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImages {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String url_image;
    private String image_id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;
}
