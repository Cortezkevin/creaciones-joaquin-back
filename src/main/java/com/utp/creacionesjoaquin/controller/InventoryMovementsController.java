package com.utp.creacionesjoaquin.controller;


import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.warehouse.InventoryMovementsDTO;
import com.utp.creacionesjoaquin.service.InventoryMovementsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
