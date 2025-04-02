package com.utp.creacionesjoaquin.administration.service;

import com.utp.creacionesjoaquin.config.cloudinary.dto.UploadDTO;
import com.utp.creacionesjoaquin.config.cloudinary.service.CloudinaryService;
import com.utp.creacionesjoaquin.config.cloudinary.utils.UploadUtils;
import com.utp.creacionesjoaquin.administration.dto.subcategory.NewSubCategoryDTO;
import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.administration.dto.subcategory.SubCategoryDTO;
import com.utp.creacionesjoaquin.administration.dto.subcategory.UpdateSubCategoryDTO;
import com.utp.creacionesjoaquin.administration.dto.product.UploadResultDTO;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.administration.model.Category;
import com.utp.creacionesjoaquin.administration.model.SubCategory;
import com.utp.creacionesjoaquin.administration.repository.CategoryRepository;
import com.utp.creacionesjoaquin.administration.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubCategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final CloudinaryService cloudinaryService;

    public ResponseWrapperDTO<List<SubCategoryDTO>> getAll(){
        List<SubCategoryDTO> subCategoryDTOList =  subCategoryRepository.findAll().stream().map( SubCategoryDTO::fromEntity ).toList();
        return ResponseWrapperDTO.<List<SubCategoryDTO>>builder()
                .status(HttpStatus.OK.name())
                .success(true)
                .message("Solicitud ejecutada correctamente")
                .content(subCategoryDTOList)
                .build();
    }

    public ResponseWrapperDTO<SubCategoryDTO> create(NewSubCategoryDTO newSubCategoryDTO, File file){
        String id = UUID.randomUUID().toString();
        UploadDTO uploadDTO = new UploadDTO( file, id );
        UploadResultDTO uploadResultDTO = cloudinaryService.upload( "sub_category", uploadDTO );
        if( uploadResultDTO != null){
            Category category = categoryRepository.findById(newSubCategoryDTO.category_id()).orElseThrow(() -> new RuntimeException("ERROR: CATEGORY NOT FOUNDED"));
            SubCategory newSubCategory = SubCategory.builder()
                    .id(id)
                    .name(newSubCategoryDTO.name())
                    .description(newSubCategoryDTO.description())
                    .url_image(uploadResultDTO.url())
                    .category( category )
                    .build();
            SubCategory subCategoryCreated = subCategoryRepository.save( newSubCategory );
            SubCategoryDTO subCategoryDTO = SubCategoryDTO.fromEntity( subCategoryCreated );
            return ResponseWrapperDTO.<SubCategoryDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .message("Sub Categoria creada satisfactoriamente")
                    .content(subCategoryDTO)
                    .build();
        }
        return null;
    }

    public ResponseWrapperDTO<SubCategoryDTO> update(UpdateSubCategoryDTO updateSubCategoryDTO, File file){
        try {
            SubCategory subCategory = subCategoryRepository.findById( updateSubCategoryDTO.id() ).orElseThrow(() -> new ResourceNotFoundException("Sub Categoria no encontrada"));
            if( file != null ){
                UploadDTO uploadDTO = new UploadDTO( file, subCategory.getId() );
                boolean imageDeleted = cloudinaryService.delete("sub_category"+"/"+ UploadUtils.formatFileName(subCategory.getName()));
                if( imageDeleted ){
                    UploadResultDTO uploadResultDTO = cloudinaryService.upload("sub_category", uploadDTO);
                    subCategory.setUrl_image( uploadResultDTO.url() );
                }
            }
            subCategory.setName(updateSubCategoryDTO.newName());
            if( subCategory.getCategory().getId() != updateSubCategoryDTO.newCategoryId() ){
                Category category = categoryRepository.findById( updateSubCategoryDTO.newCategoryId()).orElseThrow(() -> new RuntimeException(""));
                subCategory.setCategory( category );
            }
            SubCategory subCategoryUpdated = subCategoryRepository.save( subCategory );
            SubCategoryDTO subCategoryDTO = SubCategoryDTO.fromEntity( subCategoryUpdated );
            return ResponseWrapperDTO.<SubCategoryDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .message("Sub Categoria creada satisfactoriamente")
                    .content(subCategoryDTO)
                    .build();
        }catch (Exception e){
            return ResponseWrapperDTO.<SubCategoryDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .message("Ocurrio un error")
                    .content(null)
                    .build();
        }
    }
}
