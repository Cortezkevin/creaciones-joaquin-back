package com.utp.creacionesjoaquin.warehouse.controller;

import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.warehouse.dto.grocer.GrocerDTO;
import com.utp.creacionesjoaquin.warehouse.dto.grocer.NewGrocerDTO;
import com.utp.creacionesjoaquin.warehouse.service.GrocerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/grocer")
@RequiredArgsConstructor
public class GrocerController {

    private final GrocerService grocerService;

    @PreAuthorize("hasAnyAuthority('ROLE_WAREHOUSE','ROLE_TRANSPORT','ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<ResponseWrapperDTO<List<GrocerDTO>>> getAll(){
        return ResponseEntity.ok( grocerService.getAll() );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseWrapperDTO<GrocerDTO>> create(
            @RequestBody NewGrocerDTO newGrocerDTO
    ){
        return ResponseEntity.ok( grocerService.create( newGrocerDTO ) );
    }

}
