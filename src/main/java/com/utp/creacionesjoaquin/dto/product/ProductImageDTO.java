package com.utp.creacionesjoaquin.dto.product;

import com.utp.creacionesjoaquin.model.ProductImages;

public record ProductImageDTO(
        String id,
        String image_url,
        String product
) {
    public static ProductImageDTO fromEntity(ProductImages productImages){
        return new ProductImageDTO(
                productImages.getId(),
                productImages.getUrl_image(),
                productImages.getProduct().getName()
        );
    }
}
