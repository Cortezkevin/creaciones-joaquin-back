package com.utp.creacionesjoaquin.dto.cart;

public record RemoveItemDTO(
        String cart_id,
        String item_id,
        boolean removeAll,
        Integer amount
) {
}
