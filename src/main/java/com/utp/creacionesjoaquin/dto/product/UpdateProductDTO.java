package com.utp.creacionesjoaquin.dto.product;

import java.math.BigDecimal;

public record UpdateProductDTO(
        String id,
        String newName,
        String newDescription,
        BigDecimal newPrice,
        Integer newStock,
        String newSubCategoryId,
        String newCollectionId,
        String newSupplierId
) {
}
