package com.utp.creacionesjoaquin.administration.dto.collection;

public record UpdateCollectionDTO(
        String id,
        String newCategoryId,
        String newName
) {
}
