package com.utp.creacionesjoaquin.administration.dto.product;

import com.utp.creacionesjoaquin.administration.dto.subcategory.SubCategoryDTO;
import com.utp.creacionesjoaquin.administration.dto.collection.CollectionDTO;
import com.utp.creacionesjoaquin.commons.enums.ProductType;
import com.utp.creacionesjoaquin.administration.model.Product;
import com.utp.creacionesjoaquin.administration.model.ProductImages;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record ProductDTO(
        String id,
        String name,
        String description,
        SubCategoryDTO subCategory,
        CollectionDTO collection,
        String supplier,
        String supplierId,
        BigDecimal price,
        Integer stock,
        ProductType type,
        List<String> images
) {
    public static ProductDTO fromEntity(Product product){
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                SubCategoryDTO.fromEntity(product.getSubCategory()),
                product.getCollection() != null ? CollectionDTO.fromEntity(product.getCollection()) : null,
                product.getSupplier() != null ? product.getSupplier().getName() : null,
                product.getSupplier() != null ? product.getSupplier().getId() : null,
                product.getPrice(),
                product.getStock(),
                product.getType(),
                product.getProductImagesList() != null ? product.getProductImagesList().stream().map( ProductImages::getUrl_image ).collect(Collectors.toList()) : new ArrayList<>()
        );
    }
}
