package com.utp.creacionesjoaquin.purchase.dto.rawMaterial;

import com.utp.creacionesjoaquin.commons.enums.MeasurementUnit;

import java.math.BigDecimal;

public record CreateRawMaterialDTO(
        String name,
        String description,
        BigDecimal unitPrice,
        MeasurementUnit measurementUnit,
        String supplierId
) {
}
