package com.utp.creacionesjoaquin.purchase.dto.purchaseOrder;

import java.math.BigDecimal;

public record NewPurchaseDetailDTO(
        String materialOrProductId,
        Integer amount,
        BigDecimal unitPrice
) {
}
