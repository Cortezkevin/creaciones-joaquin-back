package com.utp.creacionesjoaquin.dto.order;

import com.utp.creacionesjoaquin.enums.OrderStatus;
import com.utp.creacionesjoaquin.enums.PaymentMethod;
import com.utp.creacionesjoaquin.enums.ShippingStatus;
import com.utp.creacionesjoaquin.model.Order;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderDTO(
        String id,
        BigDecimal total,
        String user,
        String shippingAddress,
        LocalDate createdDate,
        LocalDate cancelledDate,
        LocalDate completedDate,
        PaymentMethod paymentMethod,
        ShippingStatus shippingStatus,
        OrderStatus status
) {
    public static OrderDTO parseToDTO(Order order){
        return new OrderDTO(
                order.getId(),
                order.getTotal(),
                order.getUser().getPersonalInformation().getFullName(),
                order.getAddress(),
                order.getCreatedDate(),
                order.getCancelledDate(),
                order.getCompletedDate(),
                order.getPaymentMethod(),
                order.getShippingStatus(),
                order.getStatus()
        );
    }
}
