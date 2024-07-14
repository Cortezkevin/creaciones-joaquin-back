package com.utp.creacionesjoaquin.dto.dashboard;

import java.math.BigDecimal;

public record SalesByMonth(
        Integer month,
        BigDecimal sales
) {
}
