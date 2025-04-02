package com.utp.creacionesjoaquin.commons.dto;

import com.utp.creacionesjoaquin.administration.dto.category.DetailedCategoryDTO;

import java.util.List;

public record NavListDTO(
        List<DetailedCategoryDTO> categoryList
) {
}
