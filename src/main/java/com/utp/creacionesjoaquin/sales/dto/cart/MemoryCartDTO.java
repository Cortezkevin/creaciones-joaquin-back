package com.utp.creacionesjoaquin.sales.dto.cart;

import java.math.BigDecimal;
import java.util.List;

public record MemoryCartDTO(
        List<MemoryItemDTO> itemList,
        BigDecimal shippingCost
) {
}
