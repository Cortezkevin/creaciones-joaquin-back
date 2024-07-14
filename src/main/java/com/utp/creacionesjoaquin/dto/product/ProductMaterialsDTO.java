package com.utp.creacionesjoaquin.dto.product;

import com.utp.creacionesjoaquin.enums.MeasurementUnit;
import com.utp.creacionesjoaquin.model.ProductMaterials;

public record ProductMaterialsDTO(
        String materialId,
        String name,
        Integer amount,
        MeasurementUnit measurementUnit
) {
    public static ProductMaterialsDTO parseToDTO(ProductMaterials productMaterials){
       return new ProductMaterialsDTO(
               productMaterials.getRawMaterial().getId(),
               productMaterials.getRawMaterial().getName(),
               productMaterials.getAmountNeeded(),
               productMaterials.getRawMaterial().getMeasurementUnit()
       );
    }
}
