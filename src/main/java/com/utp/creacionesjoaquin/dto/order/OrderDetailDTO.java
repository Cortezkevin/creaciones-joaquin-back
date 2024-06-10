package com.utp.creacionesjoaquin.dto.order;

import com.utp.creacionesjoaquin.model.OrderDetail;

import java.math.BigDecimal;

public record OrderDetailDTO(
        String id,
        String image,
        String name,
        BigDecimal price,
        Integer amount,
        BigDecimal total
) {
    public static OrderDetailDTO parseToDTO(OrderDetail orderDetail){
        return new OrderDetailDTO(
                orderDetail.getId(),
                orderDetail.getProduct().getProductImagesList().get(0).getUrl_image(),
                orderDetail.getName(),
                orderDetail.getPrice(),
                orderDetail.getAmount(),
                orderDetail.getTotal()
        );
    }
}
