package com.utp.creacionesjoaquin.order.dto.order;

import com.utp.creacionesjoaquin.commons.enums.PreparationStatus;
import com.utp.creacionesjoaquin.order.model.OrderPreparation;

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
