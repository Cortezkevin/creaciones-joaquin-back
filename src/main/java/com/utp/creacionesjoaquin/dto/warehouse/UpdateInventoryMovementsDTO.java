package com.utp.creacionesjoaquin.dto.warehouse;

import com.utp.creacionesjoaquin.enums.InventoryMovementType;

public record UpdateInventoryMovementsDTO(
        String id,
        InventoryMovementType type,
        Integer amount,
        String reason,
        String productOrMaterialId,
        String warehouse
) {
}
