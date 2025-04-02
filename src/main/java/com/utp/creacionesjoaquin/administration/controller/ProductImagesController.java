package com.utp.creacionesjoaquin.administration.controller;

import com.utp.creacionesjoaquin.administration.dto.product.ProductImageDTO;
import com.utp.creacionesjoaquin.config.cloudinary.utils.UploadUtils;
import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.administration.service.ProductImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/product_images")
@RequiredArgsConstructor
public class ProductImagesController {
    private final ProductImagesService productImagesService;

    @GetMapping("/public")
    public ResponseEntity<ResponseWrapperDTO<List<ProductImageDTO>>> getAll(){
        return ResponseEntity.ok( productImagesService.findAll() );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping( consumes = { MediaType.MULTIPART_FORM_DATA_VALUE } )
    public ResponseEntity<ResponseWrapperDTO<ProductImageDTO>> create(
            @RequestParam("product_id") String productId,
            @RequestPart("file") MultipartFile multipartFile
    ){
        File fileToUpload = UploadUtils.getFileFromMultipartFile(multipartFile);
        return ResponseEntity.ok( productImagesService.create(productId, fileToUpload));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<ResponseWrapperDTO<ProductImageDTO>> update(
            @RequestParam("product_image_id") String productImageId,
            @RequestPart("file") MultipartFile multipartFile
    ){
        File fileToUpload = UploadUtils.getFileFromMultipartFile(multipartFile);
        return ResponseEntity.ok( productImagesService.update( productImageId, fileToUpload ));
    }
}
