package com.utp.creacionesjoaquin.security.dto;

public record JwtTokenDTO (
        String token,
        UserDTO user
){}
