package com.utp.creacionesjoaquin.order.dto.order;

public record CompletedOrderPreparationDTO(
        String orderPreparationId,
        String observations,
        String warehouse
) {
}
