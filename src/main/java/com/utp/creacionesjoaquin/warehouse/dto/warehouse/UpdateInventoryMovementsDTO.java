package com.utp.creacionesjoaquin.warehouse.dto.warehouse;

import com.utp.creacionesjoaquin.commons.enums.InventoryMovementType;

public record UpdateInventoryMovementsDTO(
        String id,
        InventoryMovementType type,
        Integer amount,
        String reason,
        String productOrMaterialId,
        String warehouse
) {
}
