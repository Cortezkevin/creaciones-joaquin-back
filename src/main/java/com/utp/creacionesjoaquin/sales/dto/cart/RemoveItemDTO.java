package com.utp.creacionesjoaquin.sales.dto.cart;

public record RemoveItemDTO(
        String cart_id,
        String item_id,
        boolean removeAll,
        Integer amount
) {
}
