package com.utp.creacionesjoaquin.warehouse.service;

import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.warehouse.dto.warehouse.WarehouseDTO;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.warehouse.model.Warehouse;
import com.utp.creacionesjoaquin.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public ResponseWrapperDTO<List<WarehouseDTO>> getAll(){
        List<WarehouseDTO> warehouseDTOList = warehouseRepository.findAll().stream().map( WarehouseDTO::parseToDTO ).toList();
        return ResponseWrapperDTO.<List<WarehouseDTO>>builder()
                .message("Solicitud satisfactoria")
                .status(HttpStatus.OK.name())
                .success(true)
                .content( warehouseDTOList )
                .build();
    }

    public ResponseWrapperDTO<WarehouseDTO> create(String location){
        Warehouse newWarehouse = Warehouse.builder()
                .location( location )
                .build();
        Warehouse warehouse = warehouseRepository.save( newWarehouse );
        return ResponseWrapperDTO.<WarehouseDTO>builder()
                .message("Almacen creado")
                .status(HttpStatus.OK.name())
                .success(true)
                .content(WarehouseDTO.parseToDTO(warehouse))
                .build();
    }

    public ResponseWrapperDTO<WarehouseDTO> update(WarehouseDTO warehouseDTO){
        try {
            Warehouse warehouse = warehouseRepository.findById( warehouseDTO.id() ).orElseThrow(() -> new ResourceNotFoundException("Almacen no encontrado"));
            warehouse.setLocation(warehouseDTO.location());
            Warehouse warehouseUpdated = warehouseRepository.save(warehouse);
            return ResponseWrapperDTO.<WarehouseDTO>builder()
                    .message("Almacen actualizado")
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .content(WarehouseDTO.parseToDTO(warehouseUpdated))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<WarehouseDTO>builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success(false)
                    .content(null)
                    .build();
        }
    }
}
