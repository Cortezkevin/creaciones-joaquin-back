package com.utp.creacionesjoaquin.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record LoginUserDTO (
        @NotBlank(message = "Required") @Email( message = "Invalid") String email,
        @NotBlank(message = "Required") String password
){
}
