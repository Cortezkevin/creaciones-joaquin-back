package com.utp.creacionesjoaquin.dto.order;

import org.springframework.http.HttpHeaders;

public record InvoiceDTO(
        Integer invoiceLength,
        byte[] resource,
        HttpHeaders headers
) {
}
