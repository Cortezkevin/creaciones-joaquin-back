package com.utp.creacionesjoaquin.administration.controller;

import com.utp.creacionesjoaquin.config.cloudinary.utils.UploadUtils;
import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.administration.dto.category.CategoryDTO;
import com.utp.creacionesjoaquin.commons.dto.NavListDTO;
import com.utp.creacionesjoaquin.administration.dto.category.UpdateCategoryDTO;
import com.utp.creacionesjoaquin.administration.service.CategoryService;
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
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/public")
    public ResponseEntity<ResponseWrapperDTO<List<CategoryDTO>>> getAll(){
        return ResponseEntity.ok( categoryService.getAll() );
    }

    @GetMapping("/nav")
    public ResponseEntity<NavListDTO> getNav(){
        return ResponseEntity.ok( categoryService.getNav() );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = "/{name}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResponseWrapperDTO<CategoryDTO>> create(
            @PathVariable String name,
            @RequestPart("file") MultipartFile multipartFile
    ){
        File fileToUpload = UploadUtils.getFileFromMultipartFile( multipartFile );
        return ResponseEntity.ok( categoryService.create( name, fileToUpload));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResponseWrapperDTO<CategoryDTO>> update(
            @RequestPart(name = "file", required = false) MultipartFile multipartFile,
            @RequestPart("updateCategoryDTO") String updateCategoryDTOString
    ){
        UpdateCategoryDTO updateCategoryDTO = UploadUtils.convertStringToObject( updateCategoryDTOString, UpdateCategoryDTO.class );
        File fileToUpload = UploadUtils.getFileFromMultipartFile( multipartFile );
        return ResponseEntity.ok( categoryService.update(updateCategoryDTO, fileToUpload) );
    }
}
