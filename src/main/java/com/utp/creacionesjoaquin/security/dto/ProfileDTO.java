package com.utp.creacionesjoaquin.security.dto;

import com.utp.creacionesjoaquin.profile.dto.address.AddressDTO;

public record ProfileDTO(
        String birthDate,
        AddressDTO address,
        String phone
) {
}
