package com.utp.creacionesjoaquin.purchase.dto.purchaseOrder;


import java.util.List;

public record CreatePurchaseOrderDTO(
        String userId,
        String supplierId,
        List<NewPurchaseDetailDTO> details
) {
}
