package com.utp.creacionesjoaquin.purchase.dto.purchaseOrder;

import com.utp.creacionesjoaquin.commons.enums.MeasurementUnit;
import com.utp.creacionesjoaquin.commons.enums.PurchaseOrderDetailStatus;
import com.utp.creacionesjoaquin.purchase.model.PurchaseOrderDetail;

import java.math.BigDecimal;

public record PurchaseOrderDetailDTO (
        String id,
        String name,
        Integer amount,
        MeasurementUnit measurementUnit,
        PurchaseOrderDetailStatus status,
        BigDecimal unitPrice,
        BigDecimal total
){
    public static PurchaseOrderDetailDTO parseToDTO(PurchaseOrderDetail purchaseOrderDetail){
        return new PurchaseOrderDetailDTO(
                purchaseOrderDetail.getId(),
                purchaseOrderDetail.getProduct() != null ? purchaseOrderDetail.getProduct().getName() : purchaseOrderDetail.getRawMaterial().getName(),
                purchaseOrderDetail.getAmount(),
                purchaseOrderDetail.getProduct() != null ? MeasurementUnit.CANTIDAD : purchaseOrderDetail.getRawMaterial().getMeasurementUnit(),
                purchaseOrderDetail.getStatus(),
                purchaseOrderDetail.getUnitPrice(),
                purchaseOrderDetail.getTotal()
        );
    }
}
