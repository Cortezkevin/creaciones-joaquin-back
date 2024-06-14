package com.utp.creacionesjoaquin.service;

import com.utp.creacionesjoaquin.cloudinary.dto.UploadDTO;
import com.utp.creacionesjoaquin.cloudinary.service.CloudinaryService;
import com.utp.creacionesjoaquin.cloudinary.utils.UploadUtils;
import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.collection.CollectionDTO;
import com.utp.creacionesjoaquin.dto.collection.NewCollectionDTO;
import com.utp.creacionesjoaquin.dto.collection.UpdateCollectionDTO;
import com.utp.creacionesjoaquin.dto.product.UploadResultDTO;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.model.Category;
import com.utp.creacionesjoaquin.model.Collection;

import com.utp.creacionesjoaquin.repository.CategoryRepository;
import com.utp.creacionesjoaquin.repository.CollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CategoryRepository categoryRepository;
    private final CollectionRepository collectionRepository;
    private final CloudinaryService cloudinaryService;

    public ResponseWrapperDTO<List<CollectionDTO>> getAll(){
        List<CollectionDTO> collectionDTOList = collectionRepository.findAll().stream().map( CollectionDTO::fromEntity ).toList();
        return ResponseWrapperDTO.<List<CollectionDTO>>builder()
                .status(HttpStatus.OK.name())
                .success(true)
                .message("Solicitud ejecutada correctamente")
                .content(collectionDTOList)
                .build();
    }

    public ResponseWrapperDTO<CollectionDTO> create(NewCollectionDTO newCollectionDTO, File file){
        try {
            String id = UUID.randomUUID().toString();
            UploadDTO uploadDTO = new UploadDTO(file, id);
            UploadResultDTO uploadResultDTO = cloudinaryService.upload( "collection", uploadDTO );
            if( uploadResultDTO != null ){
                Category category = categoryRepository.findById(newCollectionDTO.category_id()).orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada"));
                Collection newCollection = Collection.builder()
                        .id(id)
                        .name( newCollectionDTO.name() )
                        .url_image( uploadResultDTO.url() )
                        .category( category )
                        .build();
                Collection collectionCreated = collectionRepository.save( newCollection );
                CollectionDTO collectionDTO = CollectionDTO.fromEntity( collectionCreated );
                return ResponseWrapperDTO.<CollectionDTO>builder()
                        .status(HttpStatus.OK.name())
                        .success(true)
                        .message("Coleccion creada satisfactoriamente")
                        .content(collectionDTO)
                        .build();
            }
            return ResponseWrapperDTO.<CollectionDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(false)
                    .message("Ocurrio un error al procesar la imagen")
                    .content(null)
                    .build();
        }catch (Exception e){
            return ResponseWrapperDTO.<CollectionDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .message("Ocurrio un error")
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<CollectionDTO> update(UpdateCollectionDTO updateCollectionDTO, File file){
        try{
            Collection collection = collectionRepository.findById( updateCollectionDTO.id() ).orElseThrow(() -> new ResourceNotFoundException("Coleccion no encontrada"));
            if( file != null ){
                UploadDTO uploadDTO = new UploadDTO(file, collection.getId());
                boolean imageDeleted = cloudinaryService.delete("collection"+"/"+ UploadUtils.formatFileName(collection.getName()));
                if( imageDeleted ){
                    UploadResultDTO uploadResultDTO = cloudinaryService.upload("collection", uploadDTO);
                    collection.setUrl_image( uploadResultDTO.url() );
                }
            }
            collection.setName(updateCollectionDTO.newName());
            if( collection.getCategory().getId() != updateCollectionDTO.newCategoryId() ){
                Category category = categoryRepository.findById( updateCollectionDTO.newCategoryId()).orElseThrow(() -> new RuntimeException(""));
                collection.setCategory( category );
            }
            Collection collectionUpdated = collectionRepository.save( collection );
            CollectionDTO collectionDTO = CollectionDTO.fromEntity( collectionUpdated );
            return ResponseWrapperDTO.<CollectionDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .message("Coleccion actualizada satisfactoriamente")
                    .content(collectionDTO)
                    .build();
        }catch (Exception e){
            return ResponseWrapperDTO.<CollectionDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(false)
                    .message("Ocurrio un error")
                    .content(null)
                    .build();
        }
    }
}
