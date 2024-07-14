package com.utp.creacionesjoaquin.dto.order;

public record CompletedOrderPreparationDTO(
        String orderPreparationId,
        String observations,
        String warehouse
) {
}
