package com.utp.creacionesjoaquin.warehouse.dto.guide;

import com.utp.creacionesjoaquin.commons.enums.MeasurementUnit;
import com.utp.creacionesjoaquin.warehouse.model.InventoryMovements;
import lombok.Data;

import java.text.SimpleDateFormat;

@Data
public class EntryGuideMovement {
    private String movementDate;
    private String name;
    private Integer amount;
    private String unitType;

    public EntryGuideMovement() {
    }

    public EntryGuideMovement(String movementDate, String name, Integer amount, String unitType) {
        this.movementDate = movementDate;
        this.name = name;
        this.amount = amount;
        this.unitType = unitType;
    }

    public static EntryGuideMovement parseToDTO(InventoryMovements inventoryMovements){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaFormateada = dateFormat.format(inventoryMovements.getDate());
        return new EntryGuideMovement(
                fechaFormateada,
                inventoryMovements.getRawMaterial() != null ? inventoryMovements.getRawMaterial().getName() : inventoryMovements.getProduct().getName(),
                inventoryMovements.getAmount(),
                inventoryMovements.getProduct() != null ? MeasurementUnit.CANTIDAD.name() : inventoryMovements.getRawMaterial().getMeasurementUnit().name()
        );
    }
}
