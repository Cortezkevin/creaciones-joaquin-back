package com.utp.creacionesjoaquin.dto.dashboard;

import com.utp.creacionesjoaquin.enums.OrderStatus;

public record OrdersCountByStatus(
        OrderStatus status,
        Long amount
) {
}
