package com.utp.creacionesjoaquin.cloudinary.dto;

import java.io.File;
import java.io.InputStream;

public record UploadDTO(
        File file,
        InputStream inputStream,
        String name
) {
}
