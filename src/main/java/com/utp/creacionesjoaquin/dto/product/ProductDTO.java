package com.utp.creacionesjoaquin.dto.product;

import com.utp.creacionesjoaquin.dto.SubCategoryDTO;
import com.utp.creacionesjoaquin.dto.category.CategoryDTO;
import com.utp.creacionesjoaquin.dto.collection.CollectionDTO;
import com.utp.creacionesjoaquin.model.Product;
import com.utp.creacionesjoaquin.model.ProductImages;

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
        BigDecimal price,
        Integer stock,
        List<String> images
) {
    public static ProductDTO fromEntity(Product product){
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                SubCategoryDTO.fromEntity(product.getSubCategory()),
                product.getCollection() != null ? CollectionDTO.fromEntity(product.getCollection()) : null,
                product.getPrice(),
                product.getStock(),
                product.getProductImagesList() != null ? product.getProductImagesList().stream().map( ProductImages::getUrl_image ).collect(Collectors.toList()) : new ArrayList<>()
        );
    }
}