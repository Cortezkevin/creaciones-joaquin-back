package com.utp.creacionesjoaquin.purchase.dto.supplier;

import com.utp.creacionesjoaquin.purchase.model.Supplier;

public record SupplierDTO (
        String id,
        String name,
        String ruc,
        String phone,
        String address
){
    public static SupplierDTO parseToDTO(Supplier supplier){
        return new SupplierDTO(
                supplier.getId(),
                supplier.getName(),
                supplier.getRuc(),
                supplier.getPhone(),
                supplier.getAddress()
        );
    }
}
