package com.utp.creacionesjoaquin.dto.user;

import java.util.List;

public record UpdateUserDTO (
        String userId,
        String firstName,
        String lastName,
        String email,
        List<String> roles
){
}
