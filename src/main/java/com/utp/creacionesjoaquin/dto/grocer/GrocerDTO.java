package com.utp.creacionesjoaquin.dto.grocer;


import com.utp.creacionesjoaquin.enums.GrocerStatus;

import com.utp.creacionesjoaquin.model.Grocer;

public record GrocerDTO(
        String id,
        String fullName,
        String email,
        String userId,
        GrocerStatus status
) {
    public static GrocerDTO parseToDTO(Grocer grocer){
        return new GrocerDTO(
                grocer.getId(),
                grocer.getUser().getPersonalInformation().getFullName(),
                grocer.getUser().getEmail(),
                grocer.getUser().getId(),
                grocer.getStatus()
        );
    }
}
