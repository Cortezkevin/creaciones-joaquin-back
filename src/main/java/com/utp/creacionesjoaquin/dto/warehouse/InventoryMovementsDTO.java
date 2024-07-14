package com.utp.creacionesjoaquin.dto.warehouse;

import com.utp.creacionesjoaquin.enums.InventoryMovementType;
import com.utp.creacionesjoaquin.model.InventoryMovements;

import java.sql.Timestamp;

public record InventoryMovementsDTO(
        String id,
        InventoryMovementType type,
        String grocer,
        Integer initialStock,
        Integer amount,
        Integer newStock,
        Timestamp date,
        String reason,
        String productOrMaterial,
        String warehouse
) {
    public static InventoryMovementsDTO parseToDTO(InventoryMovements inventoryMovements){
        return new InventoryMovementsDTO(
                inventoryMovements.getId(),
                inventoryMovements.getType(),
                inventoryMovements.getEntryGuide() != null ? inventoryMovements.getEntryGuide().getGrocer().getUser().getPersonalInformation().getFullName() : inventoryMovements.getExitGuide().getGrocer().getUser().getPersonalInformation().getFullName(),
                inventoryMovements.getInitialStock(),
                inventoryMovements.getAmount(),
                inventoryMovements.getNewStock(),
                inventoryMovements.getDate(),
                inventoryMovements.getReason(),
                inventoryMovements.getRawMaterial() != null ? inventoryMovements.getRawMaterial().getName() : inventoryMovements.getProduct().getName(),
                inventoryMovements.getWarehouse() != null ? inventoryMovements.getWarehouse().getLocation() : "No aplica"
        );
    }
}
