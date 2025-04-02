package com.utp.creacionesjoaquin.config.cloudinary.dto;

import java.io.File;

public record UploadDTO(
        File file,
        String name
) {
}
