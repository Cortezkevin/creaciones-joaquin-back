package com.utp.creacionesjoaquin.purchase.dto.purchaseOrder;

public record OrderReceptionItemDTO (
        boolean accept,
        PurchaseOrderDetailDTO purchaseOrderDetail
){
}
