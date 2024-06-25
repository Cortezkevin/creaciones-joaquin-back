package com.utp.creacionesjoaquin.controller;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.supplier.CreateSupplierDTO;
import com.utp.creacionesjoaquin.dto.supplier.SupplierDTO;
import com.utp.creacionesjoaquin.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/supplier")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<ResponseWrapperDTO<List<SupplierDTO>>> getAll(){
        return ResponseEntity.ok( supplierService.getAll() );
    }

    @PostMapping
    public ResponseEntity<ResponseWrapperDTO<SupplierDTO>> create(
            @RequestBody CreateSupplierDTO createSupplierDTO
            ){
        return ResponseEntity.ok( supplierService.create( createSupplierDTO ) );
    }

    @PutMapping
    public ResponseEntity<ResponseWrapperDTO<SupplierDTO>> update(
            @RequestBody SupplierDTO supplierDTO
    ){
        return ResponseEntity.ok( supplierService.update( supplierDTO ) );
    }
}
