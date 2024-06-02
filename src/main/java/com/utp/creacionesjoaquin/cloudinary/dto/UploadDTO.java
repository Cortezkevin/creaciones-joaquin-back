package com.utp.creacionesjoaquin.cloudinary.dto;

import java.io.File;

public record UploadDTO(
        File file,
        String name
) {
}
