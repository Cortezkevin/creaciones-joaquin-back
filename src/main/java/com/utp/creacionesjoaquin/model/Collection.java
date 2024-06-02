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
public class Collection {
    @Id
    private String id;
    private String name;
    private String url_image;
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;
    @OneToMany(mappedBy = "collection", fetch = FetchType.LAZY)
    private List<Product> productList = new ArrayList<>();
}
