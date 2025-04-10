package com.utp.creacionesjoaquin.purchase.dto.purchaseOrder;

import com.utp.creacionesjoaquin.warehouse.dto.grocer.GrocerDTO;
import com.utp.creacionesjoaquin.purchase.dto.supplier.SupplierDTO;
import com.utp.creacionesjoaquin.commons.enums.PurchaseOrderReceptionStatus;
import com.utp.creacionesjoaquin.purchase.model.PurchaseOrderReception;

import java.sql.Timestamp;
import java.util.List;

public record DetailedPurchaseOrderReceptionDTO(
        String id,
        String observations,
        Timestamp createdDate,
        Timestamp startDate,
        Timestamp reviewDate,
        Timestamp completedDate,
        PurchaseOrderReceptionStatus status,
        SupplierDTO supplier,
        GrocerDTO grocer,
        List<PurchaseOrderDetailDTO> purchaseOrderDetails
) {
    public static DetailedPurchaseOrderReceptionDTO parseToDTO(PurchaseOrderReception purchaseOrderReception){
        return new DetailedPurchaseOrderReceptionDTO(
                purchaseOrderReception.getId(),
                purchaseOrderReception.getObservations(),
                purchaseOrderReception.getCreatedDate(),
                purchaseOrderReception.getStartDate(),
                purchaseOrderReception.getReviewDate(),
                purchaseOrderReception.getCompletedDate(),
                purchaseOrderReception.getStatus(),
                SupplierDTO.parseToDTO( purchaseOrderReception.getPurchaseOrder().getSupplier() ),
                GrocerDTO.parseToDTO( purchaseOrderReception.getGrocer() ),
                purchaseOrderReception.getPurchaseOrder().getPurchaseOrderDetails().stream().map(PurchaseOrderDetailDTO::parseToDTO).toList()
        );
    }
}
