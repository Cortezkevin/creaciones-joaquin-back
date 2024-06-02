package com.utp.creacionesjoaquin.security.dto;

import com.utp.creacionesjoaquin.dto.address.MemoryAddressDTO;
import com.utp.creacionesjoaquin.dto.cart.MemoryCartDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NewUserDTO (
        @NotBlank(message = "Required") String firstName,
        @NotBlank(message = "Required") String lastName,
        @Email(message = "Invalid") String email,
        @NotBlank(message = "Required") String password,
        MemoryCartDTO memoryCart,
        MemoryAddressDTO memoryAddress,
        Boolean isAdmin
){
}
