package com.utp.creacionesjoaquin.administration.dto.fabrication;

import java.util.List;

public record CreateFabricationProductDTO(
        String productId,
        Integer amount,
        List<String> operatorsIds
) {
}
