package com.utp.creacionesjoaquin.dto.order;

import com.utp.creacionesjoaquin.dto.grocer.GrocerDTO;
import com.utp.creacionesjoaquin.enums.PreparationStatus;
import com.utp.creacionesjoaquin.model.OrderPreparation;

import java.sql.Date;
import java.sql.Timestamp;

public record OrderPreparationDTO(
        String id,
        String userIdFromGrocer,
        String orderId,
        GrocerDTO grocer,
        Timestamp createdDate,
        Timestamp startDate,
        Timestamp preparedDate,
        Timestamp completedDate,
        PreparationStatus status
) {
    public static OrderPreparationDTO parseToDTO(OrderPreparation orderPreparation) {
        return new OrderPreparationDTO(
                orderPreparation.getId(),
                orderPreparation.getGrocer() != null ? orderPreparation.getGrocer().getUser().getId() : null,
                orderPreparation.getOrder().getId(),
                orderPreparation.getGrocer() != null ? GrocerDTO.parseToDTO(orderPreparation.getGrocer()) : null,
                orderPreparation.getCreatedDate(),
                orderPreparation.getStartDate(),
                orderPreparation.getPreparedDate(),
                orderPreparation.getCompletedDate(),
                orderPreparation.getStatus()
        );
    }
}
