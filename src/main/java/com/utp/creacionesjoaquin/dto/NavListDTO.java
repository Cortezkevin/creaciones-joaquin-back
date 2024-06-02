package com.utp.creacionesjoaquin.dto;

import com.utp.creacionesjoaquin.dto.category.DetailedCategoryDTO;

import java.util.List;

public record NavListDTO(
        List<DetailedCategoryDTO> categoryList
) {
}
