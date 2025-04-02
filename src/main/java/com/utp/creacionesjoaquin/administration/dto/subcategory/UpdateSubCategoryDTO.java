package com.utp.creacionesjoaquin.administration.dto.subcategory;

public record UpdateSubCategoryDTO(
        String id,
        String newName,
        String newDescription,
        String newCategoryId
) {
}
