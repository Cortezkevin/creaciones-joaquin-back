package com.utp.creacionesjoaquin.security.dto;

import com.utp.creacionesjoaquin.dto.address.AddressDTO;

public record ProfileDTO(
        String birthDate,
        AddressDTO address,
        String phone
) {
}
