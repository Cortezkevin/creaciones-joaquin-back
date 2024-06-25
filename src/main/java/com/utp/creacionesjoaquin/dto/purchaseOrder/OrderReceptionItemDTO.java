package com.utp.creacionesjoaquin.dto.purchaseOrder;

import java.math.BigDecimal;

public record OrderReceptionItemDTO (
        boolean accept,
        PurchaseOrderDetailDTO purchaseOrderDetail
){
}
