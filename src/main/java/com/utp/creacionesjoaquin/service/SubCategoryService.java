package com.utp.creacionesjoaquin.service;

import com.utp.creacionesjoaquin.cloudinary.dto.UploadDTO;
import com.utp.creacionesjoaquin.cloudinary.service.CloudinaryService;
import com.utp.creacionesjoaquin.cloudinary.utils.UploadUtils;
import com.utp.creacionesjoaquin.dto.NewSubCategoryDTO;
import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.SubCategoryDTO;
import com.utp.creacionesjoaquin.dto.UpdateSubCategoryDTO;
import com.utp.creacionesjoaquin.dto.collection.CollectionDTO;
import com.utp.creacionesjoaquin.dto.collection.UpdateCollectionDTO;
import com.utp.creacionesjoaquin.dto.product.ProductDTO;
import com.utp.creacionesjoaquin.dto.product.UploadResultDTO;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.model.Category;
import com.utp.creacionesjoaquin.model.Collection;
import com.utp.creacionesjoaquin.model.SubCategory;
import com.utp.creacionesjoaquin.repository.CategoryRepository;
import com.utp.creacionesjoaquin.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        UploadDTO uploadDTO = new UploadDTO( file, null, id );
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
                UploadDTO uploadDTO = new UploadDTO( file, null, subCategory.getId() );
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
