package com.utp.creacionesjoaquin.administration.controller;

import com.utp.creacionesjoaquin.config.cloudinary.utils.UploadUtils;
import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.administration.dto.product.NewProductDTO;
import com.utp.creacionesjoaquin.administration.dto.product.ProductDTO;
import com.utp.creacionesjoaquin.administration.dto.product.UpdateProductDTO;
import com.utp.creacionesjoaquin.administration.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/public")
    public ResponseEntity<ResponseWrapperDTO<List<ProductDTO>>> getAll(){
        return ResponseEntity.ok( productService.getAll() );
    }

    @GetMapping("/bySupplier")
    public ResponseEntity<ResponseWrapperDTO<List<ProductDTO>>> getAll(
            @RequestParam(name = "supplier") String supplierId
    ){
        return ResponseEntity.ok( productService.getBySupplier( supplierId ) );
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<ResponseWrapperDTO<ProductDTO>> getById(
            @PathVariable("id") String id
    ){
        return ResponseEntity.ok( productService.getById( id ) );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping( consumes = { MediaType.MULTIPART_FORM_DATA_VALUE } )
    public ResponseEntity<ResponseWrapperDTO<ProductDTO>> create(
            @RequestPart("newProductDTO") String newProductDTOString,
            @RequestPart("files") List<MultipartFile> multipartFiles
            ){
        NewProductDTO newProductDTO = UploadUtils.convertStringToObject( newProductDTOString, NewProductDTO.class );
        List<File> filesToUpload = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            File fileToUpload = UploadUtils.getFileFromMultipartFile(multipartFile);
            filesToUpload.add(fileToUpload);
            System.out.println("MULTIPART FILE: " + multipartFile.getName());
        }
        return ResponseEntity.ok( productService.create(newProductDTO, filesToUpload));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/addToCollection")
    public ResponseEntity<ResponseWrapperDTO<ProductDTO>> addToCollection(
            @RequestParam(name = "collection") String collection_id,
            @RequestParam(name = "product") String product_id
    ){
        return ResponseEntity.ok( productService.addToCollection(product_id, collection_id));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<ResponseWrapperDTO<ProductDTO>> update(
            @RequestBody UpdateProductDTO updateProductDTO
    ){
        return ResponseEntity.ok( productService.update( updateProductDTO ));
    }
}
