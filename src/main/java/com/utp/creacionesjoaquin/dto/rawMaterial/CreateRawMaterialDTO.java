package com.utp.creacionesjoaquin.dto.rawMaterial;

import com.utp.creacionesjoaquin.enums.MeasurementUnit;

import java.math.BigDecimal;

public record CreateRawMaterialDTO(
        String name,
        String description,
        BigDecimal unitPrice,
        MeasurementUnit measurementUnit,
        String supplierId
) {
}
