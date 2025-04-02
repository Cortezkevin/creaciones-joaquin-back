package com.utp.creacionesjoaquin.warehouse.dto.warehouse;

import com.utp.creacionesjoaquin.commons.enums.MeasurementUnit;
import com.utp.creacionesjoaquin.warehouse.model.InventoryMovements;

import java.sql.Timestamp;

public record MinimalInventoryMovementDTO(
        String id,
        MeasurementUnit unitType,
        Integer amount,
        Timestamp date,
        String productOrMaterial
) {
    public static MinimalInventoryMovementDTO parseToDTO(InventoryMovements inventoryMovements){
        return new MinimalInventoryMovementDTO(
                inventoryMovements.getId(),
                inventoryMovements.getProduct() != null ? MeasurementUnit.CANTIDAD : inventoryMovements.getRawMaterial().getMeasurementUnit(),
                inventoryMovements.getAmount(),
                inventoryMovements.getDate(),
                inventoryMovements.getRawMaterial() != null ? inventoryMovements.getRawMaterial().getName() : inventoryMovements.getProduct().getName()
        );
    }
}
