package com.utp.creacionesjoaquin.purchase.dto.purchaseOrder;

import com.utp.creacionesjoaquin.purchase.dto.supplier.SupplierDTO;
import com.utp.creacionesjoaquin.commons.enums.PurchaseOrderStatus;
import com.utp.creacionesjoaquin.purchase.model.PurchaseOrder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public record DetailedPurchaseOrderDTO(
        String id,
        Timestamp date,
        PurchaseOrderStatus status,
        BigDecimal total,
        String requester,
        SupplierDTO supplier,
        List<PurchaseOrderDetailDTO> orderDetails,
        String userId,
        String guide
) {
    public static DetailedPurchaseOrderDTO parseToDTO(PurchaseOrder purchaseOrder){
        return new DetailedPurchaseOrderDTO(
                purchaseOrder.getId(),
                purchaseOrder.getDate(),
                purchaseOrder.getStatus(),
                purchaseOrder.getTotal(),
                purchaseOrder.getUser().getPersonalInformation().getFullName(),
                SupplierDTO.parseToDTO( purchaseOrder.getSupplier() ),
                purchaseOrder.getPurchaseOrderDetails().stream().map( PurchaseOrderDetailDTO::parseToDTO ).toList(),
                purchaseOrder.getUser().getId(),
                purchaseOrder.getEntryGuide() != null ? purchaseOrder.getEntryGuide().getId() : purchaseOrder.getRejectionGuide() != null ? purchaseOrder.getRejectionGuide().getId() : null
        );
    }
}
