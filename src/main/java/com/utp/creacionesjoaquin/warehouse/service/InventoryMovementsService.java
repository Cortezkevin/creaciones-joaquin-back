package com.utp.creacionesjoaquin.warehouse.service;

import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.commons.enums.InventoryMovementType;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.administration.model.Product;
import com.utp.creacionesjoaquin.purchase.model.RawMaterial;
import com.utp.creacionesjoaquin.purchase.repository.RawMaterialRepository;
import com.utp.creacionesjoaquin.administration.repository.ProductRepository;
import com.utp.creacionesjoaquin.warehouse.dto.warehouse.*;
import com.utp.creacionesjoaquin.warehouse.model.*;
import com.utp.creacionesjoaquin.warehouse.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryMovementsService {

    private final InventoryMovementsRepository inventoryMovementsRepository;
    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final WarehouseRepository warehouseRepository;
    private final EntryGuideRepository entryGuideRepository;
    private final ExitGuideRepository exitGuideRepository;
    private final GrocerRepository grocerRepository;

    public ResponseWrapperDTO<List<InventoryMovementsDTO>> getAll(){
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        List<InventoryMovementsDTO> inventoryMovementsDTOS = inventoryMovementsRepository.findAll(sort).stream().map(InventoryMovementsDTO::parseToDTO).toList();
        return ResponseWrapperDTO.<List<InventoryMovementsDTO>>builder()
                .message("Solicitud satisfactoria")
                .status(HttpStatus.OK.name())
                .success(true)
                .content( inventoryMovementsDTOS )
                .build();
    }

    public ResponseWrapperDTO<DetailedMovementDTO> getById(String id){
        try {
            InventoryMovements inventoryMovements = inventoryMovementsRepository.findById( id ).orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));
            return ResponseWrapperDTO.<DetailedMovementDTO>builder()
                    .message("Solicitud satisfactoria")
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success(true)
                    .content( DetailedMovementDTO.parseToDTO( inventoryMovements ) )
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<DetailedMovementDTO>builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success(false)
                    .content( null )
                    .build();
        }
    }

    public ResponseWrapperDTO<List<InventoryMovementsDTO>> create(CreateInventoryMovementDTO createInventoryMovementDTO){
        try {
            Warehouse warehouse = warehouseRepository.findById( createInventoryMovementDTO.warehouse() ).orElseThrow(() -> new ResourceNotFoundException("Almacen no encontrado"));
            Grocer grocer = grocerRepository.findById( createInventoryMovementDTO.grocerId() ).orElseThrow(() -> new ResourceNotFoundException("Almacenero no encontrado"));

            ExitGuide exitGuideCreated = null;
            EntryGuide entryGuideCreated = null;
            if( createInventoryMovementDTO.type().equals(InventoryMovementType.ENTRADA) ){
                EntryGuide entryGuide = EntryGuide.builder()
                        .productConditions(createInventoryMovementDTO.conditions())
                        .date(new Timestamp(System.currentTimeMillis()))
                        .warehouse( warehouse )
                        .grocer( grocer )
                        .build();
                entryGuideCreated = entryGuideRepository.save( entryGuide );
            }else {
                ExitGuide exitGuide = ExitGuide.builder()
                        .observations(createInventoryMovementDTO.conditions() )
                        .date(new Timestamp(System.currentTimeMillis()))
                        .warehouse( warehouse )
                        .grocer( grocer )
                        .build();
                exitGuideCreated = exitGuideRepository.save( exitGuide );
            }

            List<InventoryMovements> newInventoryMovementsList = new ArrayList<>();
            // reducir o aumentar cantidad de stock
            for (MaterialOrProductDTO mop: createInventoryMovementDTO.materialOrProducts()){
                InventoryMovements inventoryMovements = InventoryMovements.builder()
                        .type( createInventoryMovementDTO.type() )
                        .reason( createInventoryMovementDTO.reason() )
                        .date(new Timestamp(System.currentTimeMillis()))
                        .warehouse(warehouse)
                        .amount(mop.amount())
                        .exitGuide(exitGuideCreated)
                        .entryGuide(entryGuideCreated)
                        .build();

                RawMaterial rawMaterial = rawMaterialRepository.findById( mop.id() ).orElse(null);
                if( rawMaterial != null ){
                    inventoryMovements.setRawMaterial( rawMaterial );
                    inventoryMovements.setInitialStock( rawMaterial.getStock() );
                    int newStock = 0;
                    if( createInventoryMovementDTO.type().equals(InventoryMovementType.ENTRADA) ){
                        newStock = rawMaterial.getStock() + mop.amount();
                        //rawMaterial.setStock( rawMaterial.getStock() + mop.amount() );
                    }else {
                        if( rawMaterial.getStock() < mop.amount() ){
                            throw new RuntimeException("No cuenta con suficientes existencias del material: " + rawMaterial.getName());
                        }else {
                            newStock = rawMaterial.getStock() - mop.amount();
                            //rawMaterial.setStock( rawMaterial.getStock() - mop.amount() );
                        }
                    }

                    inventoryMovements.setNewStock( newStock );
                    rawMaterial.setStock( newStock );
                    rawMaterialRepository.save( rawMaterial );
                }else {
                    Product product = productRepository.findById(mop.id() ).orElse(null);
                    if( product != null ){
                        inventoryMovements.setProduct( product );
                        inventoryMovements.setInitialStock( product.getStock() );
                        int newStock = 0;
                        if( createInventoryMovementDTO.type().equals(InventoryMovementType.ENTRADA) ){
                            newStock = product.getStock() + mop.amount();
                            //product.setStock( product.getStock() + mop.amount() );
                        }else {
                            if( product.getStock() < mop.amount() ){
                                throw new RuntimeException("No cuenta con suficientes existencias del producto: " + product.getName());
                            }else {
                                newStock = product.getStock() - mop.amount();
                                //product.setStock( product.getStock() - mop.amount() );
                            }
                        }

                        inventoryMovements.setNewStock( newStock );
                        product.setStock( newStock );
                        productRepository.save( product );
                    }
                }
                newInventoryMovementsList.add( inventoryMovements );
            }

            List<InventoryMovements> inventoryMovementsCreated = inventoryMovementsRepository.saveAll(newInventoryMovementsList);

            return ResponseWrapperDTO.<List<InventoryMovementsDTO>>builder()
                    .message("Movimientos creados")
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .content( inventoryMovementsCreated.stream().map(InventoryMovementsDTO::parseToDTO).toList() )
                    .build();
        }catch (Exception e){
            return ResponseWrapperDTO.<List<InventoryMovementsDTO>>builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success(false)
                    .content( null )
                    .build();
        }
    }

    public ResponseWrapperDTO<InventoryMovementsDTO> update(UpdateInventoryMovementsDTO updateInventoryMovementsDTO){
        try {
            InventoryMovements inventoryMovements = inventoryMovementsRepository.findById( updateInventoryMovementsDTO.id() ).orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));

            if( updateInventoryMovementsDTO.warehouse() != null ){
                Warehouse warehouse = warehouseRepository.findById( updateInventoryMovementsDTO.warehouse() ).orElseThrow(() -> new ResourceNotFoundException("Almacen no encontrado"));
                inventoryMovements.setWarehouse( warehouse );
            }
            inventoryMovements.setType(updateInventoryMovementsDTO.type());
            inventoryMovements.setProduct( null );
            inventoryMovements.setRawMaterial( null );
            inventoryMovements.setReason(updateInventoryMovementsDTO.reason());
            Product product = productRepository.findById( updateInventoryMovementsDTO.productOrMaterialId() ).orElse(null);
            if( product == null){
                RawMaterial rawMaterial = rawMaterialRepository.findById( updateInventoryMovementsDTO.productOrMaterialId() ).get();
                inventoryMovements.setRawMaterial( rawMaterial );
            }else {
                inventoryMovements.setProduct( product );
            }

            InventoryMovements inventoryMovementsUpdated = inventoryMovementsRepository.save( inventoryMovements );
            return ResponseWrapperDTO.<InventoryMovementsDTO>builder()
                    .message("Movimiento actualizado")
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .content( InventoryMovementsDTO.parseToDTO( inventoryMovementsUpdated ) )
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<InventoryMovementsDTO>builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success(false)
                    .content( null )
                    .build();
        }
    }

}
