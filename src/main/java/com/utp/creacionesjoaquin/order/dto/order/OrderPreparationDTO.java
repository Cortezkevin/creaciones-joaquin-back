package com.utp.creacionesjoaquin.order.dto.order;

import com.utp.creacionesjoaquin.warehouse.dto.grocer.GrocerDTO;
import com.utp.creacionesjoaquin.commons.enums.OrderStatus;
import com.utp.creacionesjoaquin.commons.enums.PreparationStatus;
import com.utp.creacionesjoaquin.order.model.OrderPreparation;

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
        OrderStatus orderStatus,
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
                orderPreparation.getOrder().getStatus(),
                orderPreparation.getStatus()
        );
    }
}
