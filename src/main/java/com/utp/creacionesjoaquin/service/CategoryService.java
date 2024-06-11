package com.utp.creacionesjoaquin.service;

import com.utp.creacionesjoaquin.cloudinary.dto.UploadDTO;
import com.utp.creacionesjoaquin.cloudinary.service.CloudinaryService;
import com.utp.creacionesjoaquin.cloudinary.utils.UploadUtils;
import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.category.CategoryDTO;
import com.utp.creacionesjoaquin.dto.category.DetailedCategoryDTO;
import com.utp.creacionesjoaquin.dto.NavListDTO;
import com.utp.creacionesjoaquin.dto.category.UpdateCategoryDTO;
import com.utp.creacionesjoaquin.dto.product.UploadResultDTO;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.model.Category;
import com.utp.creacionesjoaquin.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;

    public ResponseWrapperDTO<List<CategoryDTO>> getAll(){
        List<CategoryDTO> categoryDTOList = categoryRepository.findAll().stream().map( CategoryDTO::fromEntity ).toList();
        return ResponseWrapperDTO.<List<CategoryDTO>>builder()
                .status(HttpStatus.OK.name())
                .success(true)
                .message("Solicitud ejecutada correctamente")
                .content(categoryDTOList)
                .build();
    }

    public NavListDTO getNav(){
        List<Category> categoryList = categoryRepository.findAll();
        return new NavListDTO(
                categoryList.stream().map(DetailedCategoryDTO::fromEntity).collect(Collectors.toList())
        );
    }

    public ResponseWrapperDTO<CategoryDTO> create(String name, File file){
        String id = UUID.randomUUID().toString();
        UploadDTO uploadDTO = new UploadDTO(file,null, id);
        UploadResultDTO uploadResultDTO = cloudinaryService.upload( "category", uploadDTO );
        if( uploadResultDTO != null ){
            Category newCategory = Category.builder()
                    .id(id)
                    .name( name ).url_image( uploadResultDTO.url() )
                    .build();
            Category categoryCreated = categoryRepository.save( newCategory );
            CategoryDTO categoryDTO = CategoryDTO.fromEntity( categoryCreated );
            return ResponseWrapperDTO.<CategoryDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .message("Categoria creada satisfactoriamente")
                    .content(categoryDTO)
                    .build();
        }
        return null;
    }

    public ResponseWrapperDTO<CategoryDTO> update(UpdateCategoryDTO updateCategoryDTO, File file){
        try{
            Category category = categoryRepository.findById( updateCategoryDTO.id() ).orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada"));
            if( file != null ){
                UploadDTO uploadDTO = new UploadDTO(file, null, category.getId());
                boolean imageDeleted = cloudinaryService.delete("category"+"/"+UploadUtils.formatFileName(category.getName()));
                if( imageDeleted ){
                    UploadResultDTO uploadResultDTO = cloudinaryService.upload("category", uploadDTO);
                    category.setUrl_image( uploadResultDTO.url() );
                }
            }
            category.setName(updateCategoryDTO.newName());
            Category categoryUpdated = categoryRepository.save( category );
            CategoryDTO categoryDTO = CategoryDTO.fromEntity( categoryUpdated );
            return ResponseWrapperDTO.<CategoryDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .message("Categoria Actualizada satisfactoriamente")
                    .content(categoryDTO)
                    .build();
        }catch (Exception e){
            return ResponseWrapperDTO.<CategoryDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(false)
                    .message("Ocurrio un error")
                    .content(null)
                    .build();
        }
    }

}


    /*public String delete(Integer category_id){
        Category category = categoryRepository.findById( category_id ).orElseThrow(() -> new RuntimeException(""));
        boolean imageDeleted = cloudinaryService.delete("category/"+UploadUtils.formatFileName(category.getName()));
        if( imageDeleted ){
            categoryRepository.deleteById(category_id);
        }
    }*/