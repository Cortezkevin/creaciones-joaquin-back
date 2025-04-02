package com.utp.creacionesjoaquin.commons.dto;

import java.sql.Timestamp;

public record UpdateDates(
        String orderId,
        Timestamp createdDate,
        Timestamp preparationDate,
        Timestamp shippingDate,
        Timestamp completedDate,
        Double distance
) {
}
