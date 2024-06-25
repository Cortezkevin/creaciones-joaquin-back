package com.utp.creacionesjoaquin.controller;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.warehouse.WarehouseDTO;
import com.utp.creacionesjoaquin.service.WarehouseService;
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
