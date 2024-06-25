package com.utp.creacionesjoaquin.service;

import com.utp.creacionesjoaquin.cloudinary.dto.UploadDTO;
import com.utp.creacionesjoaquin.cloudinary.service.CloudinaryService;
import com.utp.creacionesjoaquin.cloudinary.utils.UploadUtils;
import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.collection.CollectionDTO;
import com.utp.creacionesjoaquin.dto.product.NewProductDTO;
import com.utp.creacionesjoaquin.dto.product.ProductDTO;
import com.utp.creacionesjoaquin.dto.category.CategoryDTO;
import com.utp.creacionesjoaquin.dto.category.UpdateCategoryDTO;
import com.utp.creacionesjoaquin.dto.product.UpdateProductDTO;
import com.utp.creacionesjoaquin.dto.product.UploadResultDTO;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.model.*;
import com.utp.creacionesjoaquin.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CollectionRepository collectionRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ProductImagesRepository productImagesRepository;
    private final SupplierRepository supplierRepository;

    private final CloudinaryService cloudinaryService;

    public ResponseWrapperDTO<List<ProductDTO>> getAll(){
        List<ProductDTO> productDTOList = productRepository.findAll().stream().map( ProductDTO::fromEntity ).toList();
        return ResponseWrapperDTO.<List<ProductDTO>>builder()
                .status(HttpStatus.OK.name())
                .success(true)
                .message("Solicitud ejecutada correctamente")
                .content(productDTOList)
                .build();
    }

    public ResponseWrapperDTO<List<ProductDTO>> getBySupplier(String supplierId){
        try {
            Supplier supplier = supplierRepository.findById( supplierId ).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));
            List<ProductDTO> productDTOList = productRepository.findBySupplier( supplier ).stream().map( ProductDTO::fromEntity ).toList();
            return ResponseWrapperDTO.<List<ProductDTO>>builder()
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .message("Solicitud ejecutada correctamente")
                    .content(productDTOList)
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<List<ProductDTO>>builder()
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success(false)
                    .message(e.getMessage())
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<ProductDTO> create(NewProductDTO newProductDTO, List<File> files){
        String id = UUID.randomUUID().toString();
        List<UploadDTO> uploadDTOList = new ArrayList<>();
        for (int i = 0; i < files.size(); i ++ ){
            File file = files.get(i);
            uploadDTOList.add( new UploadDTO( file, id + "_" + (i+1) ));
        }
        List<UploadResultDTO> urlImages = cloudinaryService.uploadMany2( "product", uploadDTOList );
        if( urlImages != null){
            SubCategory subCategory = subCategoryRepository.findById(newProductDTO.subcategory_id()).orElseThrow(() -> new RuntimeException("ERROR: SUBCATEGORY NOT FOUNDED"));
            Product newProduct = Product.builder()
                    .id(id)
                    .name( newProductDTO.name() )
                    .description( newProductDTO.description() )
                    .price( newProductDTO.price() )
                    //.stock(newProductDTO.stock() ) // 0 STOCK INICIALMENTE, SI SE QUIERE AGREGAR STOCK SE DEBE REALIZAR UN MOVIMIENTO, FABRICACION
                    .subCategory( subCategory )
                    .build();

            if(newProductDTO.supplierId() != null ){
                Supplier supplier = supplierRepository.findById(newProductDTO.supplierId()).orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
                newProduct.setSupplier( supplier );
            }

            Product productCreated = productRepository.save( newProduct );

            List<ProductImages> newProductImages = urlImages.stream().map( result -> ProductImages.builder().url_image(result.url()).image_id(result.public_id()).product(productCreated).build() ).toList();
            productImagesRepository.saveAll( newProductImages );

            ProductDTO productDTO = ProductDTO.fromEntity( productCreated );
            return ResponseWrapperDTO.<ProductDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .message("Producto creado satisfactoriamente")
                    .content(productDTO)
                    .build();
        }
        return null;
    }

    public ResponseWrapperDTO<ProductDTO> addToCollection(String productId, String collectionId){
        Collection collection = collectionRepository.findById( collectionId ).orElseThrow(() -> new RuntimeException("ERROR: COLLECTION NOT FOUNDED"));
        Product product = productRepository.findById( productId ).orElseThrow(() -> new RuntimeException("ERROR: PRODUCT NOT FOUNDED"));

        product.setCollection( collection );
        Product productUpdated = productRepository.save( product );
        ProductDTO productDTO = ProductDTO.fromEntity( productUpdated );
        return ResponseWrapperDTO.<ProductDTO>builder()
                .status(HttpStatus.OK.name())
                .success(true)
                .message("Producto actualizado satisfactoriamente")
                .content(productDTO)
                .build();
    }


    public ResponseWrapperDTO<ProductDTO> update(UpdateProductDTO updateProductDTO){
        try {
            Product product = productRepository.findById( updateProductDTO.id() ).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
            product.setName(updateProductDTO.newName());
            product.setDescription(updateProductDTO.newDescription());
            product.setPrice(updateProductDTO.newPrice());
            product.setStock(updateProductDTO.newStock());

            if( product.getSubCategory().getId() != updateProductDTO.newSubCategoryId() ){
                SubCategory subCategory = subCategoryRepository.findById( updateProductDTO.newSubCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Subcategoria no encontrada"));
                product.setSubCategory( subCategory );
            }

            if( updateProductDTO.newSupplierId() != null ){
                if( product.getSupplier().getId() != updateProductDTO.newSupplierId() ){
                    Supplier supplier = supplierRepository.findById(updateProductDTO.newSupplierId()).orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
                    product.setSupplier( supplier );
                }
            }

            if(updateProductDTO.newCollectionId() != null ){
                Collection collection = collectionRepository.findById( updateProductDTO.newCollectionId()).orElseThrow(() -> new ResourceNotFoundException("Coleccion no encontrada"));
                product.setCollection( collection );
            }

            Product productUpdated = productRepository.save( product );
            ProductDTO productDTO = ProductDTO.fromEntity( productUpdated );
            return ResponseWrapperDTO.<ProductDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .message("Producto actualizado satisfactoriamente")
                    .content(productDTO)
                    .build();
        }catch (Exception e){
            return ResponseWrapperDTO.<ProductDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(false)
                    .message("Ocurrio un error: " + e.getMessage())
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<ProductDTO> getById(String id) {
        try {
            Product product = productRepository.findById( id ).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
            return ResponseWrapperDTO.<ProductDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .message("Producto cargado correctamente")
                    .content(ProductDTO.fromEntity( product ))
                    .build();
        }catch (Exception e){
            return ResponseWrapperDTO.<ProductDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .message("Ocurrio un error: " + e.getMessage())
                    .content(null)
                    .build();
        }
    }
}




        /*List<ProductImages> productImagesList = productImagesRepository.findByProduct( product );
        if( uploadDTO != null ){
            boolean imageDeleted = cloudinaryService.delete("product"+"/"+UploadUtils.formatFileName(product.getName()));
            if( imageDeleted ){
                String newUrlImage = cloudinaryService.upload("product", uploadDTO);
                product.setUrl_image( newUrlImage );
            }
        }*/