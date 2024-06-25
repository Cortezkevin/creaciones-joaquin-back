package com.utp.creacionesjoaquin.dto.purchaseOrder;

import com.utp.creacionesjoaquin.enums.PurchaseOrderStatus;
import com.utp.creacionesjoaquin.model.PurchaseOrder;


import java.math.BigDecimal;
import java.sql.Timestamp;

public record PurchaseOrderDTO(
        String id,
        Timestamp date,
        PurchaseOrderStatus status,
        BigDecimal total,
        String requester,
        String supplier,
        String purchaseOrderReceptionId,
        String supplierId,
        String userId,
        String guide
) {
    public static PurchaseOrderDTO parseToDTO(PurchaseOrder purchaseOrder){
        return new PurchaseOrderDTO(
                purchaseOrder.getId(),
                purchaseOrder.getDate(),
                purchaseOrder.getStatus(),
                purchaseOrder.getTotal(),
                purchaseOrder.getUser().getPersonalInformation().getFullName(),
                purchaseOrder.getSupplier().getName(),
                purchaseOrder.getPurchaseOrderReception().getId(),
                purchaseOrder.getSupplier().getId(),
                purchaseOrder.getUser().getId(),
                purchaseOrder.getEntryGuide() != null ? purchaseOrder.getEntryGuide().getId() : purchaseOrder.getRejectionGuide() != null ? purchaseOrder.getRejectionGuide().getId() : null
        );
    }
}
