package com.utp.creacionesjoaquin.controller;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.rawMaterial.CreateRawMaterialDTO;
import com.utp.creacionesjoaquin.dto.rawMaterial.RawMaterialDTO;

import com.utp.creacionesjoaquin.service.RawMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/raw_material")
@RequiredArgsConstructor
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    @GetMapping
    public ResponseEntity<ResponseWrapperDTO<List<RawMaterialDTO>>> getAll(){
        return ResponseEntity.ok( rawMaterialService.getAll() );
    }

    @GetMapping("/bySupplier")
    public ResponseEntity<ResponseWrapperDTO<List<RawMaterialDTO>>> getAll(
            @RequestParam(name = "supplier") String supplierId
    ){
        return ResponseEntity.ok( rawMaterialService.getBySupplier( supplierId ) );
    }

    @PostMapping
    public ResponseEntity<ResponseWrapperDTO<RawMaterialDTO>> create(
            @RequestBody CreateRawMaterialDTO createRawMaterialDTO
    ){
        return ResponseEntity.ok( rawMaterialService.create( createRawMaterialDTO ) );
    }

    @PutMapping
    public ResponseEntity<ResponseWrapperDTO<RawMaterialDTO>> update(
            @RequestBody RawMaterialDTO rawMaterialDTO
    ){
        return ResponseEntity.ok( rawMaterialService.update( rawMaterialDTO ) );
    }

}
