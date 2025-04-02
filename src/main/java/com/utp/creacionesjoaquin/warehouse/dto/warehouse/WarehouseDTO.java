package com.utp.creacionesjoaquin.warehouse.dto.warehouse;

import com.utp.creacionesjoaquin.warehouse.model.Warehouse;

public record WarehouseDTO(
        String id,
        String location
) {
    public static WarehouseDTO parseToDTO(Warehouse warehouse){
        return new WarehouseDTO(
                warehouse.getId(),
                warehouse.getLocation()
        );
    }
}
