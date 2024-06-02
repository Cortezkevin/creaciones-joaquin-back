package com.utp.creacionesjoaquin.dto;

import com.utp.creacionesjoaquin.dto.category.CategoryDTO;
import com.utp.creacionesjoaquin.model.SubCategory;

public record SubCategoryDTO (
        String id,
        String name,
        String description,
        CategoryDTO category,
        String url_image
){
    public static SubCategoryDTO fromEntity(SubCategory subCategory){
        return new SubCategoryDTO(
                subCategory.getId(),
                subCategory.getName(),
                subCategory.getDescription(),
                CategoryDTO.fromEntity( subCategory.getCategory() ),
                subCategory.getUrl_image()
        );
    }
}
