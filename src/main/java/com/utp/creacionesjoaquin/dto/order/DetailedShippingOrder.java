package com.utp.creacionesjoaquin.dto.order;

import com.utp.creacionesjoaquin.enums.ShippingStatus;
import com.utp.creacionesjoaquin.model.OrderShipping;

import java.sql.Date;
import java.sql.Timestamp;

public record DetailedShippingOrder(
        String id,
        String preparedBy,
        DetailedOrderDTO order,
        Timestamp createdDate,
        Timestamp startDate,
        Timestamp preparedDate,
        Timestamp shippingDate,
        Timestamp completedDate,
        String exitGuideId,
        ShippingStatus status
) {
    public static DetailedShippingOrder parseToDTO(OrderShipping orderShipping) {
        return new DetailedShippingOrder(
                orderShipping.getId(),
                orderShipping.getPreparedBy(),
                DetailedOrderDTO.parseToDTO(orderShipping.getOrder()),
                orderShipping.getCreatedDate(),
                orderShipping.getStartDate(),
                orderShipping.getPreparedDate(),
                orderShipping.getShippingDate(),
                orderShipping.getCompletedDate(),
                orderShipping.getOrder().getExitGuide().getId(),
                orderShipping.getStatus()
        );
    }
}
