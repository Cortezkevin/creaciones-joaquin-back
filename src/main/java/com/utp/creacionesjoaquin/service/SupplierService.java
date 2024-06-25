package com.utp.creacionesjoaquin.service;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.rawMaterial.CreateRawMaterialDTO;
import com.utp.creacionesjoaquin.dto.rawMaterial.RawMaterialDTO;
import com.utp.creacionesjoaquin.dto.supplier.CreateSupplierDTO;
import com.utp.creacionesjoaquin.dto.supplier.SupplierDTO;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.model.Supplier;
import com.utp.creacionesjoaquin.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public ResponseWrapperDTO<List<SupplierDTO>> getAll(){
        List<SupplierDTO> supplierDTOS = supplierRepository.findAll().stream().map(SupplierDTO::parseToDTO).toList();
        return ResponseWrapperDTO.<List<SupplierDTO>>builder()
                .message("Solicitud satisfactoria")
                .status(HttpStatus.OK.name())
                .success(true)
                .content( supplierDTOS )
                .build();
    }

    public ResponseWrapperDTO<SupplierDTO> create(CreateSupplierDTO createSupplierDTO){
        Supplier newSupplier = Supplier.builder()
                .name(createSupplierDTO.name())
                .address(createSupplierDTO.address())
                .ruc(createSupplierDTO.ruc())
                .phone(createSupplierDTO.phone())
                .build();
        Supplier supplierCreated = supplierRepository.save( newSupplier );
        return ResponseWrapperDTO.<SupplierDTO>builder()
                .message("Proveedor creado")
                .status(HttpStatus.OK.name())
                .success(true)
                .content(SupplierDTO.parseToDTO(supplierCreated))
                .build();
    }

    public ResponseWrapperDTO<SupplierDTO> update(SupplierDTO supplierDTO){
        try {
            Supplier supplier = supplierRepository.findById( supplierDTO.id() ).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));
            supplier.setName(supplierDTO.name());
            supplier.setAddress(supplierDTO.address());
            supplier.setPhone(supplierDTO.phone());
            supplier.setRuc(supplierDTO.ruc());
            Supplier supplierUpdated = supplierRepository.save(supplier);
            return ResponseWrapperDTO.<SupplierDTO>builder()
                    .message("Proveedor actualizado")
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .content(SupplierDTO.parseToDTO(supplierUpdated))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<SupplierDTO>builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success(false)
                    .content(null)
                    .build();
        }
    }
}
