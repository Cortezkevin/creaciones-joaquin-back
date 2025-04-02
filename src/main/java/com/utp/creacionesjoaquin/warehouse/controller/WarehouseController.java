package com.utp.creacionesjoaquin.warehouse.controller;

import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.warehouse.dto.warehouse.WarehouseDTO;
import com.utp.creacionesjoaquin.warehouse.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping
    public ResponseEntity<ResponseWrapperDTO<List<WarehouseDTO>>> getAll(){
        return ResponseEntity.ok( warehouseService.getAll() );
    }

    @PostMapping
    public ResponseEntity<ResponseWrapperDTO<WarehouseDTO>> create(
            @RequestParam(name = "location") String location
    ){
        return ResponseEntity.ok( warehouseService.create( location ) );
    }

    @PutMapping
    public ResponseEntity<ResponseWrapperDTO<WarehouseDTO>> update(
            @RequestBody WarehouseDTO warehouseDTO
    ){
        return ResponseEntity.ok( warehouseService.update( warehouseDTO ) );
    }

}
