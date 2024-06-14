package com.utp.creacionesjoaquin.dto.order;

import com.utp.creacionesjoaquin.enums.PreparationStatus;
import com.utp.creacionesjoaquin.model.OrderPreparation;

import java.sql.Timestamp;

public record DetailedPreparationOrder(
        String id,
        DetailedOrderDTO order,
        Timestamp createdDate,
        Timestamp startDate,
        Timestamp preparedDate,
        Timestamp completedDate,
        PreparationStatus status
) {
    public static DetailedPreparationOrder parseToDTO(OrderPreparation orderPreparation) {
        return new DetailedPreparationOrder(
                orderPreparation.getId(),
                DetailedOrderDTO.parseToDTO( orderPreparation.getOrder()),
                orderPreparation.getCreatedDate(),
                orderPreparation.getStartDate(),
                orderPreparation.getPreparedDate(),
                orderPreparation.getCompletedDate(),
                orderPreparation.getStatus()
        );
    }
}
