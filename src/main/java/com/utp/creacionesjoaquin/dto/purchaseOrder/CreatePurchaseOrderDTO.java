package com.utp.creacionesjoaquin.dto.purchaseOrder;


import java.util.List;

public record CreatePurchaseOrderDTO(
        String userId,
        String supplierId,
        List<NewPurchaseDetailDTO> details
) {
}
