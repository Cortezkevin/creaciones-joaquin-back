package com.utp.creacionesjoaquin.service;

import com.utp.creacionesjoaquin.cloudinary.dto.UploadDTO;
import com.utp.creacionesjoaquin.cloudinary.service.CloudinaryService;
import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.collection.CollectionDTO;
import com.utp.creacionesjoaquin.dto.product.ProductImageDTO;
import com.utp.creacionesjoaquin.dto.product.UploadResultDTO;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.model.Collection;
import com.utp.creacionesjoaquin.model.Product;
import com.utp.creacionesjoaquin.model.ProductImages;
import com.utp.creacionesjoaquin.repository.ProductImagesRepository;
import com.utp.creacionesjoaquin.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductImagesService {

    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;
    private final ProductImagesRepository productImagesRepository;

    public ResponseWrapperDTO<List<ProductImageDTO>> findAll(){
        List<ProductImageDTO> productImageDTOList = productImagesRepository.findAll().stream().map(ProductImageDTO::fromEntity).toList();
        return ResponseWrapperDTO.<List<ProductImageDTO>>builder()
                .status(HttpStatus.OK.name())
                .success(true)
                .message("Solicitud ejecutada correctamente")
                .content(productImageDTOList)
                .build();
    }

    public ResponseWrapperDTO<ProductImageDTO> create(String productId, File file){
        try {
            Product product = productRepository.findById( productId ).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
            long productImagesCount = productImagesRepository.countByProduct(product);
            UploadDTO uploadDTO = new UploadDTO(file,null, product.getId() + "_" + (productImagesCount + 1));
            UploadResultDTO uploadResultDTO = cloudinaryService.upload("product", uploadDTO);
            if( uploadResultDTO != null ){
                ProductImages newProductImage = ProductImages.builder()
                        .product(product)
                        .image_id(uploadResultDTO.public_id())
                        .url_image(uploadResultDTO.url())
                        .build();
                ProductImages productImageCreated = productImagesRepository.save( newProductImage );
                ProductImageDTO productImageDTO = ProductImageDTO.fromEntity( productImageCreated );
                return ResponseWrapperDTO.<ProductImageDTO>builder()
                        .status(HttpStatus.OK.name())
                        .success(true)
                        .message("Imagen creada satisfactoriamente")
                        .content(productImageDTO)
                        .build();
            }
            return ResponseWrapperDTO.<ProductImageDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(false)
                    .message("Ocurrio un error al procesar la imagen")
                    .content(null)
                    .build();
        }catch (Exception e){
            return ResponseWrapperDTO.<ProductImageDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .message("Ocurrio un error")
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<ProductImageDTO> update(String productImageId, File file){
        try {
            ProductImages productImage = productImagesRepository.findById( productImageId ).orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada"));
            UploadDTO uploadDTO = new UploadDTO(file,null, productImage.getImage_id().replace("product/",""));

            boolean result = cloudinaryService.delete( productImage.getImage_id() );
            if( result ){
                UploadResultDTO uploadResultDTO = cloudinaryService.upload("product", uploadDTO);
                if(uploadResultDTO != null) {
                    productImage.setImage_id(uploadResultDTO.public_id());
                    productImage.setUrl_image(uploadResultDTO.url());
                    ProductImages productImageUpdated = productImagesRepository.save( productImage );
                    ProductImageDTO productImageDTO = ProductImageDTO.fromEntity( productImageUpdated );
                    return ResponseWrapperDTO.<ProductImageDTO>builder()
                            .status(HttpStatus.OK.name())
                            .success(true)
                            .message("Imagen actualizada satisfactoriamente")
                            .content(productImageDTO)
                            .build();
                }
                return ResponseWrapperDTO.<ProductImageDTO>builder()
                        .status(HttpStatus.OK.name())
                        .success(false)
                        .message("Ocurrio un error al procesar la imagen")
                        .content(null)
                        .build();
            }
            return ResponseWrapperDTO.<ProductImageDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(false)
                    .message("Ocurrio un error al procesar la imagen")
                    .content(null)
                    .build();
        }catch (Exception e) {
            return ResponseWrapperDTO.<ProductImageDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .message("Ocurrio un error")
                    .content(null)
                    .build();
        }
    }
}
