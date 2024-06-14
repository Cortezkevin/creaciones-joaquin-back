package com.utp.creacionesjoaquin.dto.order;

import com.utp.creacionesjoaquin.dto.carrier.CarrierDTO;
import com.utp.creacionesjoaquin.enums.ShippingStatus;
import com.utp.creacionesjoaquin.model.OrderShipping;

import java.sql.Date;
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
