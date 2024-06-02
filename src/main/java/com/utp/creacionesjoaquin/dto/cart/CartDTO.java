package com.utp.creacionesjoaquin.dto.cart;

import com.utp.creacionesjoaquin.model.Cart;

import java.math.BigDecimal;
import java.util.List;

public record CartDTO(
        String id,
        BigDecimal tax,
        BigDecimal discount,
        BigDecimal subtotal,
        BigDecimal shippingCost,
        BigDecimal total,
        List<CartItemDTO> cartItems,
        String user_id
) {
    public static CartDTO fromEntity(Cart cart){
        return new CartDTO(
                cart.getId(),
                cart.getTax(),
                cart.getDiscount(),
                cart.getSubtotal(),
                cart.getShippingCost(),
                cart.getTotal(),
                cart.getCartItems().stream().map( CartItemDTO::fromEntity ).toList(),
                cart.getUser().getId()
        );
    }
}
