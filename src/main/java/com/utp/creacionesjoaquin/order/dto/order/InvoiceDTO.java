package com.utp.creacionesjoaquin.order.dto.order;

import org.springframework.http.HttpHeaders;

public record InvoiceDTO(
        Integer invoiceLength,
        byte[] resource,
        HttpHeaders headers
) {
}
