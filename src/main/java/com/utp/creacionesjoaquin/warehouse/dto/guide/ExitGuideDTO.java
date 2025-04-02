package com.utp.creacionesjoaquin.warehouse.dto.guide;

import com.utp.creacionesjoaquin.warehouse.model.ExitGuide;

import java.sql.Timestamp;

public record ExitGuideDTO(
        String id,
        Timestamp date,
        String observations,
        String grocer,
        String order,
        String warehouse
) {
    public static ExitGuideDTO parseToDTO(ExitGuide exitGuide){
        return new ExitGuideDTO(
                exitGuide.getId(),
                exitGuide.getDate(),
                exitGuide.getObservations(),
                exitGuide.getGrocer().getUser().getPersonalInformation().getFullName(),
                exitGuide.getOrder() != null ? exitGuide.getOrder().getId() : "No aplica",
                exitGuide.getWarehouse().getLocation()
        );
    }
}
