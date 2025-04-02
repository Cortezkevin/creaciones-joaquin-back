package com.utp.creacionesjoaquin.administration.controller;

import com.utp.creacionesjoaquin.config.cloudinary.utils.UploadUtils;
import com.utp.creacionesjoaquin.administration.dto.subcategory.NewSubCategoryDTO;
import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.administration.dto.subcategory.SubCategoryDTO;
import com.utp.creacionesjoaquin.administration.dto.subcategory.UpdateSubCategoryDTO;
import com.utp.creacionesjoaquin.administration.service.SubCategoryService;
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
@RequestMapping("/api/sub-category")
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @GetMapping("/public")
    public ResponseEntity<ResponseWrapperDTO<List<SubCategoryDTO>>> getAll(){
        return ResponseEntity.ok( subCategoryService.getAll() );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping( consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResponseWrapperDTO<SubCategoryDTO>> create(
            @RequestPart("newSubCategory") String newSubCategoryString,
            @RequestPart("file") MultipartFile multipartFile
    ){
        NewSubCategoryDTO newSubCategoryDTO = UploadUtils.convertStringToObject( newSubCategoryString, NewSubCategoryDTO.class );
        File fileToUpload = UploadUtils.getFileFromMultipartFile( multipartFile );
        return ResponseEntity.ok( subCategoryService.create(newSubCategoryDTO, fileToUpload));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResponseWrapperDTO<SubCategoryDTO>> update(
            @RequestPart(name = "file", required = false) MultipartFile multipartFile,
            @RequestPart("updateSubCategoryDTO") String updateSubCategoryDTOString
    ){
        UpdateSubCategoryDTO updateSubCategoryDTO = UploadUtils.convertStringToObject( updateSubCategoryDTOString, UpdateSubCategoryDTO.class );
        File fileToUpload = UploadUtils.getFileFromMultipartFile( multipartFile );
        return ResponseEntity.ok( subCategoryService.update(updateSubCategoryDTO, fileToUpload) );
    }
}
