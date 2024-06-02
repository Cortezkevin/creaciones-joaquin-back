package com.utp.creacionesjoaquin.dto.address;


import com.utp.creacionesjoaquin.model.Address;

public record AddressDTO(
     String id,
     Double lta,
     Double lng,
     String department,
     String province,
     String district,
     String urbanization,
     String street,
     Integer postalCode,
     String fullAddress
) {
    public static AddressDTO parseToDTO(Address address){
        return new AddressDTO(
                address.getId(),
                address.getLta(),
                address.getLng(),
                address.getDepartment(),
                address.getProvince(),
                address.getDistrict(),
                address.getUrbanization(),
                address.getStreet(),
                address.getPostalCode(),
                address.getFullAddress()
        );
    }
}
