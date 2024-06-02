package com.utp.creacionesjoaquin.dto.cart;

import java.math.BigDecimal;

public record UpdateShippingCostDTO(
        BigDecimal shippingCost,
        String cartId
) {
}
