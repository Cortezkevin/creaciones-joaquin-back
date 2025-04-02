package com.utp.creacionesjoaquin.profile.dto.user;

public record UpdateProfile(
        String userId,
        String firstName,
        String lastName,
        String email,
        String phone,
        String birthdate,
        String photoUrl
) {
}
