package com.utp.creacionesjoaquin.administration.dto.product;

import java.math.BigDecimal;
import java.util.List;

public record NewProductDTO(
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String subcategory_id,
        String supplierId,
        List<ProductMaterialsDTO> materials
) {
}
