package com.utp.creacionesjoaquin.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    private String id;
    private String name;
    private String url_image;
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Collection> collectionList = new ArrayList<>();
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<SubCategory> subCategoryList = new ArrayList<>();
}
