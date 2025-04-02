package com.utp.creacionesjoaquin.purchase.service;

import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.purchase.dto.rawMaterial.CreateRawMaterialDTO;
import com.utp.creacionesjoaquin.purchase.dto.rawMaterial.RawMaterialDTO;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.purchase.model.RawMaterial;
import com.utp.creacionesjoaquin.purchase.model.Supplier;
import com.utp.creacionesjoaquin.purchase.repository.RawMaterialRepository;
import com.utp.creacionesjoaquin.purchase.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;
    private final SupplierRepository supplierRepository;

    public ResponseWrapperDTO<List<RawMaterialDTO>> getAll(){
        List<RawMaterialDTO> rawMaterialDTOList = rawMaterialRepository.findAll().stream().map(RawMaterialDTO::parseToDTO).toList();
        return ResponseWrapperDTO.<List<RawMaterialDTO>>builder()
                .message("Solicitud satisfactoria")
                .status(HttpStatus.OK.name())
                .success(true)
                .content( rawMaterialDTOList )
                .build();
    }

    public ResponseWrapperDTO<List<RawMaterialDTO>> getBySupplier(String supplierId){
        try {
            Supplier supplier = supplierRepository.findById( supplierId ).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));
            List<RawMaterialDTO> rawMaterialDTOList = rawMaterialRepository.findBySupplier( supplier ).stream().map(RawMaterialDTO::parseToDTO).toList();
            return ResponseWrapperDTO.<List<RawMaterialDTO>>builder()
                    .message("Solicitud satisfactoria")
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .content( rawMaterialDTOList )
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<List<RawMaterialDTO>>builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success(false)
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<RawMaterialDTO> create(CreateRawMaterialDTO createRawMaterialDTO){
        try {
            Supplier supplier = supplierRepository.findById(createRawMaterialDTO.supplierId()).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));
            RawMaterial newRawMaterial = RawMaterial.builder()
                    .description(createRawMaterialDTO.description())
                    .name(createRawMaterialDTO.name())
                    .measurementUnit(createRawMaterialDTO.measurementUnit())
                    .unitPrice(createRawMaterialDTO.unitPrice())
                    .supplier(supplier)
                    .stock(0)
                    .build();
            RawMaterial rawMaterialCreated = rawMaterialRepository.save( newRawMaterial );
            return ResponseWrapperDTO.<RawMaterialDTO>builder()
                    .message("Material creado")
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .content(RawMaterialDTO.parseToDTO(rawMaterialCreated))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<RawMaterialDTO>builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success(false)
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<RawMaterialDTO> update(RawMaterialDTO rawMaterialDTO){
        try {
            RawMaterial rawMaterial = rawMaterialRepository.findById( rawMaterialDTO.id() ).orElseThrow(() -> new ResourceNotFoundException("Material no encontrado"));
            rawMaterial.setName(rawMaterialDTO.name());
            rawMaterial.setDescription(rawMaterialDTO.description());
            rawMaterial.setUnitPrice(rawMaterialDTO.unitPrice());
            rawMaterial.setMeasurementUnit(rawMaterialDTO.measurementUnit());

            if( rawMaterial.getSupplier() != null || rawMaterial.getSupplier().getId() != rawMaterialDTO.id()){
                Supplier supplier = supplierRepository.findById(rawMaterialDTO.supplierId()).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));
                rawMaterial.setSupplier( supplier );
            }

            RawMaterial rawMaterialCreated = rawMaterialRepository.save( rawMaterial );
            return ResponseWrapperDTO.<RawMaterialDTO>builder()
                    .message("Material actualizado")
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .content(RawMaterialDTO.parseToDTO(rawMaterialCreated))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<RawMaterialDTO>builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success(false)
                    .content(null)
                    .build();
        }
    }
}
