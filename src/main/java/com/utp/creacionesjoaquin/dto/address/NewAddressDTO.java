package com.utp.creacionesjoaquin.dto.address;

public record NewAddressDTO(
    Double lta,
    Double lng,
    String department,
    String province,
    String district,
    String urbanization,
    String street,
    Integer postalCode,
    String fullAddress,
    String userId
) {
}
