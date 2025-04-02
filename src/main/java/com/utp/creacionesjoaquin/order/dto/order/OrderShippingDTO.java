package com.utp.creacionesjoaquin.order.dto.order;

import com.utp.creacionesjoaquin.warehouse.dto.carrier.CarrierDTO;
import com.utp.creacionesjoaquin.commons.enums.ShippingStatus;
import com.utp.creacionesjoaquin.order.model.OrderShipping;

import java.sql.Timestamp;

public record OrderShippingDTO (
        String id,
        String userIdFromCarrier,
        String orderId,
        CarrierDTO carrier,
        String preparedBy,
        String address,
        Timestamp createdDate,
        Timestamp startDate,
        Timestamp preparedDate,
        Timestamp shippingDate,
        Timestamp completedDate,
        ShippingStatus status
){
    public static OrderShippingDTO parseToDTO(OrderShipping orderShipping) {
        return new OrderShippingDTO(
                orderShipping.getId(),
                orderShipping.getCarrier() != null ? orderShipping.getCarrier().getUser().getId() : null,
                orderShipping.getOrder().getId(),
                orderShipping.getCarrier() != null ? CarrierDTO.parseToDTO(orderShipping.getCarrier()) : null,
                orderShipping.getPreparedBy(),
                orderShipping.getAddress(),
                orderShipping.getCreatedDate(),
                orderShipping.getStartDate(),
                orderShipping.getPreparedDate(),
                orderShipping.getShippingDate(),
                orderShipping.getCompletedDate(),
                orderShipping.getStatus()
        );
    }
}
