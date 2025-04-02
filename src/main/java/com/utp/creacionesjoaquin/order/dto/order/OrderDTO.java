package com.utp.creacionesjoaquin.order.dto.order;

import com.utp.creacionesjoaquin.commons.enums.OrderStatus;
import com.utp.creacionesjoaquin.commons.enums.PaymentMethod;
import com.utp.creacionesjoaquin.commons.enums.PreparationStatus;
import com.utp.creacionesjoaquin.commons.enums.ShippingStatus;
import com.utp.creacionesjoaquin.order.model.Order;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record OrderDTO(
        String id,
        BigDecimal total,
        String user,
        String shippingAddress,
        Timestamp createdDate,
        Timestamp cancelledDate,
        Timestamp completedDate,
        PaymentMethod paymentMethod,
        PreparationStatus preparationStatus,
        ShippingStatus shippingStatus,
        OrderStatus status
) {
    public static OrderDTO parseToDTO(Order order){
        return new OrderDTO(
                order.getId(),
                order.getTotal(),
                order.getUser().getPersonalInformation().getFullName(),
                order.getShippingAddress(),
                order.getCreatedDate(),
                order.getCancelledDate(),
                order.getCompletedDate(),
                order.getPaymentMethod(),
                order.getOrderPreparation() != null ? order.getOrderPreparation().getStatus() : PreparationStatus.PENDIENTE,
                order.getOrderShipping() != null ? order.getOrderShipping().getStatus() : ShippingStatus.PENDIENTE,
                order.getStatus()
        );
    }
}
