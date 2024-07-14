package com.utp.creacionesjoaquin.dto.warehouse;

import com.utp.creacionesjoaquin.enums.InventoryMovementType;

import java.util.List;

public record CreateInventoryMovementDTO(
        InventoryMovementType type,
        String reason,
        String conditions,
        List<MaterialOrProductDTO> materialOrProducts,
        String grocerId,
        String warehouse
) {
}
