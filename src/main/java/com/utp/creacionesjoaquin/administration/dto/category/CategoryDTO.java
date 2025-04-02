package com.utp.creacionesjoaquin.administration.dto.category;
import com.utp.creacionesjoaquin.administration.model.Category;

public record CategoryDTO(
        String id,
        String name,
        String url_image
        //List<SubCategoryDTO> subCategoryDTOList,
        ///List<CollectionDTO> collectionList
) {
    public static CategoryDTO fromEntity(Category category){
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getUrl_image()
                //category.getSubCategoryList().stream().map( SubCategoryDTO::fromEntity ).collect(Collectors.toList()),
                //category.getCollectionList().stream().map( CollectionDTO::fromEntity ).collect(Collectors.toList())
        );
    }
}
