package com.utp.creacionesjoaquin.dto.order;

import com.utp.creacionesjoaquin.enums.OrderStatus;
import com.utp.creacionesjoaquin.enums.PaymentMethod;
import com.utp.creacionesjoaquin.enums.ShippingStatus;
import com.utp.creacionesjoaquin.model.Order;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public record DetailedOrderDTO(
        String id,
        String note,
        BigDecimal subtotal,
        BigDecimal shippingCost,
        BigDecimal tax,
        BigDecimal discount,
        BigDecimal total,
        UserOrderDTO user,
        String shippingAddress,
        String specificAddress,
        Timestamp createdDate,
        Timestamp cancelledDate,
        Timestamp completedDate,
        PaymentMethod paymentMethod,
        OrderPreparationDTO preparation,
        OrderShippingDTO shipping,
        OrderStatus status,
        List<OrderDetailDTO> orderDetails,
        String invoiceUrl
) {
    public static DetailedOrderDTO parseToDTO(Order order){
        return new DetailedOrderDTO(
                order.getId(),
                order.getNote(),
                order.getSubtotal(),
                order.getShippingCost(),
                order.getTax(),
                order.getDiscount(),
                order.getTotal(),
                UserOrderDTO.parseToDTO(order.getUser()),
                order.getShippingAddress(),
                order.getSpecificAddress(),
                order.getCreatedDate(),
                order.getCancelledDate(),
                order.getCompletedDate(),
                order.getPaymentMethod(),
                OrderPreparationDTO.parseToDTO(order.getOrderPreparation()),
                order.getOrderShipping() != null ? OrderShippingDTO.parseToDTO(order.getOrderShipping()) : null,
                order.getStatus(),
                order.getOrderDetails().stream().map(OrderDetailDTO::parseToDTO).toList(),
                order.getInvoice().getUrl()
        );
    }
}
