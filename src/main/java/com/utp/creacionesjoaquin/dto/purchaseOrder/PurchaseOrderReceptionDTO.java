package com.utp.creacionesjoaquin.dto.purchaseOrder;

import com.utp.creacionesjoaquin.enums.PurchaseOrderReceptionStatus;
import com.utp.creacionesjoaquin.enums.PurchaseOrderStatus;
import com.utp.creacionesjoaquin.model.PurchaseOrderReception;

import java.sql.Timestamp;

public record PurchaseOrderReceptionDTO(
        String id,
        String observations,
        Timestamp createdDate,
        Timestamp startDate,
        Timestamp reviewDate,
        Timestamp completedDate,
        PurchaseOrderStatus purchaseOrderStatus,
        PurchaseOrderReceptionStatus status,
        String purchaseOrderId,
        String grocer,
        String grocerId
) {
    public static PurchaseOrderReceptionDTO parseToDTO(PurchaseOrderReception purchaseOrderReception){
        return new PurchaseOrderReceptionDTO(
                purchaseOrderReception.getId(),
                purchaseOrderReception.getObservations(),
                purchaseOrderReception.getCreatedDate(),
                purchaseOrderReception.getStartDate(),
                purchaseOrderReception.getReviewDate(),
                purchaseOrderReception.getCompletedDate(),
                purchaseOrderReception.getPurchaseOrder().getStatus(),
                purchaseOrderReception.getStatus(),
                purchaseOrderReception.getPurchaseOrder().getId(),
                purchaseOrderReception.getGrocer() != null ? purchaseOrderReception.getGrocer().getUser().getPersonalInformation().getFullName() : "Nadie",
                purchaseOrderReception.getGrocer() != null ? purchaseOrderReception.getGrocer().getId() : null
        );
    }
}
