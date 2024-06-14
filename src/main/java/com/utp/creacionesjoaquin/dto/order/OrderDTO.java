package com.utp.creacionesjoaquin.dto.order;

import com.utp.creacionesjoaquin.enums.OrderStatus;
import com.utp.creacionesjoaquin.enums.PaymentMethod;
import com.utp.creacionesjoaquin.enums.PreparationStatus;
import com.utp.creacionesjoaquin.enums.ShippingStatus;
import com.utp.creacionesjoaquin.model.Order;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

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
