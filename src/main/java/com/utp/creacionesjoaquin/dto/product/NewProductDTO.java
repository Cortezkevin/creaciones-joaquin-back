package com.utp.creacionesjoaquin.dto.product;

import java.math.BigDecimal;

public record NewProductDTO(
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String subcategory_id,
        String supplierId
) {
}
