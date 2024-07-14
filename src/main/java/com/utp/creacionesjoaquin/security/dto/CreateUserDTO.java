package com.utp.creacionesjoaquin.security.dto;

import com.utp.creacionesjoaquin.security.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateUserDTO(
        @NotBlank(message = "Required") String firstName,
        @NotBlank(message = "Required") String lastName,
        @Email(message = "Invalid") String email,
        @NotBlank(message = "Required") String password,
        Status status,
        List<String> roles
) {
}
