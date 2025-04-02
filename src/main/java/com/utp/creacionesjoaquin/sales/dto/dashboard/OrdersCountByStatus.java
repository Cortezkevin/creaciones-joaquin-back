package com.utp.creacionesjoaquin.sales.dto.dashboard;

import com.utp.creacionesjoaquin.commons.enums.OrderStatus;

public record OrdersCountByStatus(
        OrderStatus status,
        Long amount
) {
}
