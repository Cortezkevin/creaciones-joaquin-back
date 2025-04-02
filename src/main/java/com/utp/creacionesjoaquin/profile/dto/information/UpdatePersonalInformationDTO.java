package com.utp.creacionesjoaquin.profile.dto.information;

import java.time.LocalDate;

public record UpdatePersonalInformationDTO(
        String userId,
        String firstName,
        String lastName,
        String phone,
        LocalDate birthdate
) {
}
