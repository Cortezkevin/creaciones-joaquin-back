package com.utp.creacionesjoaquin.warehouse.dto.warehouse;

import com.utp.creacionesjoaquin.warehouse.dto.grocer.GrocerDTO;
import com.utp.creacionesjoaquin.administration.dto.product.ProductDTO;
import com.utp.creacionesjoaquin.purchase.dto.rawMaterial.RawMaterialDTO;
import com.utp.creacionesjoaquin.commons.enums.InventoryMovementType;
import com.utp.creacionesjoaquin.warehouse.model.InventoryMovements;

import java.sql.Timestamp;

public record DetailedMovementDTO (
        String id,
        InventoryMovementType type,
        GrocerDTO grocer,
        Integer initialStock,
        Integer amount,
        Integer newStock,
        Timestamp date,
        String reason,
        RawMaterialDTO rawMaterial,
        ProductDTO productDTO,
        String warehouse,
        String guide
){
    public static DetailedMovementDTO parseToDTO(InventoryMovements inventoryMovements){
        return new DetailedMovementDTO(
                inventoryMovements.getId(),
                inventoryMovements.getType(),
                inventoryMovements.getExitGuide() != null ? GrocerDTO.parseToDTO(inventoryMovements.getExitGuide().getGrocer()) : GrocerDTO.parseToDTO(inventoryMovements.getEntryGuide().getGrocer()),
                inventoryMovements.getInitialStock(),
                inventoryMovements.getAmount(),
                inventoryMovements.getNewStock(),
                inventoryMovements.getDate(),
                inventoryMovements.getReason(),
                inventoryMovements.getRawMaterial() != null ? RawMaterialDTO.parseToDTO(inventoryMovements.getRawMaterial()) : null,
                inventoryMovements.getProduct() != null ? ProductDTO.fromEntity(inventoryMovements.getProduct()) : null,
                inventoryMovements.getWarehouse().getLocation(),
                inventoryMovements.getEntryGuide() != null ? inventoryMovements.getEntryGuide().getId() : inventoryMovements.getExitGuide().getId()
        );
    }
}
