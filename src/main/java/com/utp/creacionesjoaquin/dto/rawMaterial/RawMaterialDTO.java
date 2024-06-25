package com.utp.creacionesjoaquin.dto.rawMaterial;

import com.utp.creacionesjoaquin.enums.MeasurementUnit;
import com.utp.creacionesjoaquin.model.RawMaterial;

import java.math.BigDecimal;

public record RawMaterialDTO(
        String id,
        String name,
        String description,
        MeasurementUnit measurementUnit,
        Integer stock,
        BigDecimal unitPrice,
        String supplier,
        String supplierId
) {
    public static RawMaterialDTO parseToDTO(RawMaterial rawMaterial){
        return new RawMaterialDTO(
                rawMaterial.getId(),
                rawMaterial.getName(),
                rawMaterial.getDescription(),
                rawMaterial.getMeasurementUnit(),
                rawMaterial.getStock(),
                rawMaterial.getUnitPrice(),
                rawMaterial.getSupplier().getName(),
                rawMaterial.getSupplier().getId()
        );
    }
}
