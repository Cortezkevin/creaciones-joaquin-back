package com.utp.creacionesjoaquin.sales.dto.dashboard;

public record OrdersAmountStatus(
        Long total,
        Long completed,
        Long cancelled,
        Long process,
        Long pending
) {
}
