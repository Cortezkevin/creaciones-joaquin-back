package com.utp.creacionesjoaquin.service;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.warehouse.InventoryMovementsDTO;
import com.utp.creacionesjoaquin.repository.InventoryMovementsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryMovementsService {

    private final InventoryMovementsRepository inventoryMovementsRepository;

    public ResponseWrapperDTO<List<InventoryMovementsDTO>> getAll(){
        List<InventoryMovementsDTO> inventoryMovementsDTOS = inventoryMovementsRepository.findAll().stream().map(InventoryMovementsDTO::parseToDTO).toList();
        return ResponseWrapperDTO.<List<InventoryMovementsDTO>>builder()
                .message("Solicitud satisfactoria")
                .status(HttpStatus.OK.name())
                .success(true)
                .content( inventoryMovementsDTOS )
                .build();
    }

}
