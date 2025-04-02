package com.utp.creacionesjoaquin.sales.dto.dashboard;

import java.math.BigDecimal;

public record SalesByMonth(
        Integer month,
        BigDecimal sales
) {
}
