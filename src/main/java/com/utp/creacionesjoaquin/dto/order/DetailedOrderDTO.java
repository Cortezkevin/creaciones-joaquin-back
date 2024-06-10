package com.utp.creacionesjoaquin.dto.order;

import com.utp.creacionesjoaquin.enums.OrderStatus;
import com.utp.creacionesjoaquin.enums.PaymentMethod;
import com.utp.creacionesjoaquin.enums.ShippingStatus;
import com.utp.creacionesjoaquin.model.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DetailedOrderDTO(
        String id,
        BigDecimal subtotal,
        BigDecimal shippingCost,
        BigDecimal tax,
        BigDecimal discount,
        BigDecimal total,
        String user,
        String shippingAddress,
        LocalDate createdDate,
        LocalDate cancelledDate,
        LocalDate completedDate,
        PaymentMethod paymentMethod,
        ShippingStatus shippingStatus,
        OrderStatus status,
        List<OrderDetailDTO> orderDetails,
        String invoiceUrl
) {
    public static DetailedOrderDTO parseToDTO(Order order){
        return new DetailedOrderDTO(
                order.getId(),
                order.getSubtotal(),
                order.getShippingCost(),
                order.getTax(),
                order.getDiscount(),
                order.getTotal(),
                order.getUser().getPersonalInformation().getFullName(),
                order.getAddress(),
                order.getCreatedDate(),
                order.getCancelledDate(),
                order.getCompletedDate(),
                order.getPaymentMethod(),
                order.getShippingStatus(),
                order.getStatus(),
                order.getOrderDetails().stream().map(OrderDetailDTO::parseToDTO).toList(),
                order.getInvoice().getUrl()
        );
    }
}
