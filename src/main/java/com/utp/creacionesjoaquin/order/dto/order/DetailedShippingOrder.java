package com.utp.creacionesjoaquin.order.dto.order;

import com.utp.creacionesjoaquin.commons.enums.ShippingStatus;
import com.utp.creacionesjoaquin.order.model.OrderShipping;

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
