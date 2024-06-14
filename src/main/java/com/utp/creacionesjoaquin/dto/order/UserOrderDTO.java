package com.utp.creacionesjoaquin.dto.order;

import com.utp.creacionesjoaquin.security.model.User;

public record UserOrderDTO(
        String fullName,
        String email,
        String phone
) {
    public static UserOrderDTO parseToDTO(User user){
        return new UserOrderDTO(
                user.getPersonalInformation().getFullName(),
                user.getEmail(),
                user.getPersonalInformation().getPhone()
        );
    }
}
