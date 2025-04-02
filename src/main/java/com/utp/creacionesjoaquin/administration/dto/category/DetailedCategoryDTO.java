package com.utp.creacionesjoaquin.administration.dto.category;

import com.utp.creacionesjoaquin.administration.dto.collection.CollectionDTO;
import com.utp.creacionesjoaquin.administration.dto.subcategory.SubCategoryDTO;
import com.utp.creacionesjoaquin.administration.model.Category;

import java.util.List;
import java.util.stream.Collectors;

public record DetailedCategoryDTO(
        String id,
        String name,
        String url_image,
        List<SubCategoryDTO> subCategoryList,
        List<CollectionDTO> collectionList
) {
    public static DetailedCategoryDTO fromEntity(Category category){
        return new DetailedCategoryDTO(
                category.getId(),
                category.getName(),
                category.getUrl_image(),
                category.getSubCategoryList().stream().map( SubCategoryDTO::fromEntity ).collect(Collectors.toList()),
                category.getCollectionList().stream().map( CollectionDTO::fromEntity ).collect(Collectors.toList())
        );
    }
}
