package com.utp.creacionesjoaquin.warehouse.dto.carrier;

import com.utp.creacionesjoaquin.commons.enums.CarrierStatus;

public record NewCarrierDTO(
        String userId,
        String plateCode,
        CarrierStatus status
) {
}
