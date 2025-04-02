package com.utp.creacionesjoaquin.warehouse.dto.guide;

import com.utp.creacionesjoaquin.warehouse.model.RejectionGuide;

import java.sql.Timestamp;

public record RejectionGuideDTO(
        String id,
        Timestamp date,
        String reason,
        String productConditions,
        String suggestions,
        String grocer,
        String purchaseOrder
) {
    public static RejectionGuideDTO parseToDTO(RejectionGuide rejectionGuide){
        return new RejectionGuideDTO(
                rejectionGuide.getId(),
                rejectionGuide.getDate(),
                rejectionGuide.getReason(),
                rejectionGuide.getProductConditions(),
                rejectionGuide.getSuggestions(),
                rejectionGuide.getGrocer().getUser().getPersonalInformation().getFullName(),
                rejectionGuide.getPurchaseOrder() != null ? rejectionGuide.getPurchaseOrder().getId() : "No aplica"
        );
    }
}
