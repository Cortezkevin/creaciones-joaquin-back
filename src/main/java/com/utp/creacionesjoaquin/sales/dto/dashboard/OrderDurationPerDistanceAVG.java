package com.utp.creacionesjoaquin.sales.dto.dashboard;

public record OrderDurationPerDistanceAVG(
        String distanceRange,
        Double avgDurationInDays
) {
}
