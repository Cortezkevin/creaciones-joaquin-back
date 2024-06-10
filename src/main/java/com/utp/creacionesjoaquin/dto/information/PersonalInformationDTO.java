package com.utp.creacionesjoaquin.dto.information;

import com.utp.creacionesjoaquin.model.PersonalInformation;

import java.time.LocalDate;

public record PersonalInformationDTO(
        String firstName,
        String lastName,
        String phone,
        LocalDate birthdate
) {
    public static PersonalInformationDTO parseToDTO(PersonalInformation personalInformationUpdated) {
        return new PersonalInformationDTO(
                personalInformationUpdated.getFirstName(),
                personalInformationUpdated.getLastName(),
                personalInformationUpdated.getPhone(),
                personalInformationUpdated.getBirthdate()
        );
    }
}
