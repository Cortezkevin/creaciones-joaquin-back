package com.utp.creacionesjoaquin.dto.user;

public record UpdateProfile(
        String userId,
        String firstName,
        String lastName,
        String phone
) {
}
