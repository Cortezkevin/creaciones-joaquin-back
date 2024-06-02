package com.utp.creacionesjoaquin.dto;

public record UpdateSubCategoryDTO(
        String id,
        String newName,
        String newDescription,
        String newCategoryId
) {
}
