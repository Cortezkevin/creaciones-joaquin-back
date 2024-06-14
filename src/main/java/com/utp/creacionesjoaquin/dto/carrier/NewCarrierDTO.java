package com.utp.creacionesjoaquin.dto.carrier;

import com.utp.creacionesjoaquin.enums.CarrierStatus;

public record NewCarrierDTO(
        String userId,
        String plateCode,
        CarrierStatus status
) {
}
