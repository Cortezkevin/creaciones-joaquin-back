package com.utp.creacionesjoaquin.commons.dto;

import com.utp.creacionesjoaquin.security.model.Role;

public record RoleDTO (
        String value,
        String key
){
    public static RoleDTO parseToDTO(Role role){
        return new RoleDTO(
                role.getRolName().getCapitalizedName(),
                role.getRolName().name()
        );
    }
}
