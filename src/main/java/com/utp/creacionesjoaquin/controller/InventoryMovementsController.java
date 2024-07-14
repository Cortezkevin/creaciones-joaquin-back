package com.utp.creacionesjoaquin.controller;


import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.warehouse.CreateInventoryMovementDTO;
import com.utp.creacionesjoaquin.dto.warehouse.DetailedMovementDTO;
import com.utp.creacionesjoaquin.dto.warehouse.InventoryMovementsDTO;
import com.utp.creacionesjoaquin.dto.warehouse.UpdateInventoryMovementsDTO;
import com.utp.creacionesjoaquin.service.InventoryMovementsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/movements")
@RequiredArgsConstructor
public class InventoryMovementsController {

    private final InventoryMovementsService inventoryMovementsService;

    @GetMapping
    public ResponseEntity<ResponseWrapperDTO<List<InventoryMovementsDTO>>> getAll(){
        return ResponseEntity.ok( inventoryMovementsService.getAll() );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapperDTO<DetailedMovementDTO>> getById(
            @PathVariable String id
    ){
        return ResponseEntity.ok( inventoryMovementsService.getById(id) );
    }

    @PostMapping
    public ResponseEntity<ResponseWrapperDTO<List<InventoryMovementsDTO>>> create(
            @RequestBody CreateInventoryMovementDTO createInventoryMovementDTO
            ){
        return ResponseEntity.ok( inventoryMovementsService.create( createInventoryMovementDTO ));
    }

    @PutMapping
    public ResponseEntity<ResponseWrapperDTO<InventoryMovementsDTO>> update(
            @RequestBody UpdateInventoryMovementsDTO updateInventoryMovementsDTO
    ){
        return ResponseEntity.ok( inventoryMovementsService.update( updateInventoryMovementsDTO ));
    }
}
