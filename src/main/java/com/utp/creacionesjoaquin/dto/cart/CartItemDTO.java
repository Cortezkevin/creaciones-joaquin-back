package com.utp.creacionesjoaquin.dto.cart;

import com.utp.creacionesjoaquin.model.CartItem;

import java.math.BigDecimal;

public record CartItemDTO(
        String id,
        String product_id,
        String name,
        String description,
        Integer stock,
        BigDecimal price,
        Integer amount,
        BigDecimal total,
        String image
) {
    public static CartItemDTO fromEntity(CartItem cartItem){
        return new CartItemDTO(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getDescription(),
                cartItem.getProduct().getStock(),
                cartItem.getProduct().getPrice(),
                cartItem.getAmount(),
                cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getAmount())),
                cartItem.getProduct().getProductImagesList().get(0).getUrl_image()
        );
    }
}
