package com.utp.creacionesjoaquin.administration.dto.product;

import com.utp.creacionesjoaquin.administration.model.ProductImages;

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
