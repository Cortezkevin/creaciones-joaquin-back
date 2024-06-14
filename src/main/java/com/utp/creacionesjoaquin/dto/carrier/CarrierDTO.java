package com.utp.creacionesjoaquin.dto.carrier;

import com.utp.creacionesjoaquin.enums.CarrierStatus;
import com.utp.creacionesjoaquin.model.Carrier;

public record CarrierDTO(
        String id,
        String fullName,
        String email,
        String phone,
        String plateCode,
        String userId,
        CarrierStatus status
) {
    public static CarrierDTO parseToDTO(Carrier carrier){
        return new CarrierDTO(
                carrier.getId(),
                carrier.getUser().getPersonalInformation().getFullName(),
                carrier.getUser().getEmail(),
                carrier.getUser().getPersonalInformation().getPhone(),
                carrier.getCodePlate(),
                carrier.getUser().getId(),
                carrier.getStatus()
        );
    }
}
