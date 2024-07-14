package com.utp.creacionesjoaquin.dto.fabrication;

import com.utp.creacionesjoaquin.enums.FabricationStatus;
import com.utp.creacionesjoaquin.model.FabricationProduct;

import java.sql.Timestamp;

public record FabricationProductDTO(
        String id,
        Timestamp startDate,
        Timestamp endDate,
        String serieNumber,
        Integer amount,
        FabricationStatus status,
        String observations,
        String product
) {
    public static FabricationProductDTO parseToDTO(FabricationProduct fabricationProduct){
        return new FabricationProductDTO(
                fabricationProduct.getId(),
                fabricationProduct.getStartDate(),
                fabricationProduct.getEndDate(),
                fabricationProduct.getSerieNumber(),
                fabricationProduct.getAmount(),
                fabricationProduct.getStatus(),
                fabricationProduct.getObservations(),
                fabricationProduct.getProduct().getName()
        );
    }
}
