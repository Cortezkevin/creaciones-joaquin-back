package com.utp.creacionesjoaquin.profile.dto.user;

import com.utp.creacionesjoaquin.security.enums.Status;

import java.util.List;

public record UpdateUserDTO (
        String userId,
        String firstName,
        String lastName,
        String email,
        Status status,
        List<String> roles
){
}
