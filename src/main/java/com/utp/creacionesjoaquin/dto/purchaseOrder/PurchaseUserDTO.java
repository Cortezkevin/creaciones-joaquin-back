package com.utp.creacionesjoaquin.dto.purchaseOrder;

import com.utp.creacionesjoaquin.security.model.User;

public record PurchaseUserDTO(
        String id,
        String fullName,
        String email
) {
    public static PurchaseUserDTO parseToDTO(User user){
        return new PurchaseUserDTO(
                user.getId(),
                user.getPersonalInformation().getFullName(),
                user.getEmail()
        );
    }
}
