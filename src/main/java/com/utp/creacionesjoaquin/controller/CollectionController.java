package com.utp.creacionesjoaquin.controller;

import com.utp.creacionesjoaquin.cloudinary.utils.UploadUtils;
import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.collection.CollectionDTO;
import com.utp.creacionesjoaquin.dto.collection.NewCollectionDTO;

import com.utp.creacionesjoaquin.dto.collection.UpdateCollectionDTO;
import com.utp.creacionesjoaquin.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/collection")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @GetMapping("/public")
    public ResponseEntity<ResponseWrapperDTO<List<CollectionDTO>>> getAll(){
        return ResponseEntity.ok( collectionService.getAll() );
    }

    @PostMapping
    public ResponseEntity<ResponseWrapperDTO<CollectionDTO>> create(
            @RequestPart(name = "file", required = false) MultipartFile multipartFile,
            @RequestPart("newCollectionDTO") String newCollectionDTOString
    ){
        NewCollectionDTO newCollectionDTO = UploadUtils.convertStringToObject(newCollectionDTOString, NewCollectionDTO.class);
        File fileToUpload = UploadUtils.getFileFromMultipartFile( multipartFile );
        return ResponseEntity.ok( collectionService.create(newCollectionDTO, fileToUpload));
    }

    @PutMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResponseWrapperDTO<CollectionDTO>> update(
            @RequestPart(name = "file", required = false) MultipartFile multipartFile,
            @RequestPart("updateCollectionDTO") String updateCollectionDTOString
    ){
        UpdateCollectionDTO updateCollectionDTO = UploadUtils.convertStringToObject( updateCollectionDTOString, UpdateCollectionDTO.class );
        File fileToUpload = UploadUtils.getFileFromMultipartFile( multipartFile );
        return ResponseEntity.ok( collectionService.update(updateCollectionDTO, fileToUpload) );
    }

}
