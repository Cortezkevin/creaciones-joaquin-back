package com.utp.creacionesjoaquin.dto.warehouse;

import com.utp.creacionesjoaquin.enums.InventoryMovementType;
import com.utp.creacionesjoaquin.model.InventoryMovements;

import java.sql.Timestamp;

public record InventoryMovementsDTO(
        String id,
        InventoryMovementType type,
        Integer amount,
        Timestamp date,
        String productOrMaterial,
        String warehouse
) {
    public static InventoryMovementsDTO parseToDTO(InventoryMovements inventoryMovements){
        return new InventoryMovementsDTO(
                inventoryMovements.getId(),
                inventoryMovements.getType(),
                inventoryMovements.getAmount(),
                inventoryMovements.getDate(),
                inventoryMovements.getRawMaterial() != null ? inventoryMovements.getRawMaterial().getName() : inventoryMovements.getProduct().getName(),
                inventoryMovements.getWarehouse() != null ? inventoryMovements.getWarehouse().getLocation() : "No aplica"
        );
    }
}
