package com.utp.creacionesjoaquin.warehouse.dto.grocer;


import com.utp.creacionesjoaquin.commons.enums.GrocerStatus;

import com.utp.creacionesjoaquin.warehouse.model.Grocer;

public record GrocerDTO(
        String id,
        String fullName,
        String email,
        String phone,
        String userId,
        GrocerStatus status
) {
    public static GrocerDTO parseToDTO(Grocer grocer){
        return new GrocerDTO(
                grocer.getId(),
                grocer.getUser().getPersonalInformation().getFullName(),
                grocer.getUser().getEmail(),
                grocer.getUser().getPersonalInformation().getPhone(),
                grocer.getUser().getId(),
                grocer.getStatus()
        );
    }
}
