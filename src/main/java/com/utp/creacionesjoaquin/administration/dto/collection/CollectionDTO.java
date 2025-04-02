package com.utp.creacionesjoaquin.administration.dto.collection;


import com.utp.creacionesjoaquin.administration.dto.category.CategoryDTO;
import com.utp.creacionesjoaquin.administration.model.Collection;

public record CollectionDTO(
        String id,
        String name,
        CategoryDTO category,
        String url_image
) {
    public static CollectionDTO fromEntity(Collection collection){
        return new CollectionDTO(
                collection.getId(),
                collection.getName(),
                CategoryDTO.fromEntity( collection.getCategory() ),
                collection.getUrl_image()
        );
    }
}
