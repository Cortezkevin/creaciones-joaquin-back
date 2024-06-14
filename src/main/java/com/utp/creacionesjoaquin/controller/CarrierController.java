package com.utp.creacionesjoaquin.controller;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.carrier.CarrierDTO;
import com.utp.creacionesjoaquin.dto.carrier.NewCarrierDTO;
import com.utp.creacionesjoaquin.service.CarrierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/carrier")
@RequiredArgsConstructor
public class CarrierController {

    private final CarrierService carrierService;

    @PreAuthorize("hasAnyAuthority('ROLE_WAREHOUSE','ROLE_TRANSPORT','ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<ResponseWrapperDTO<List<CarrierDTO>>> getAll(){
        return ResponseEntity.ok( carrierService.getAll() );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseWrapperDTO<CarrierDTO>> create(
            @RequestBody NewCarrierDTO newCarrierDTO
    ){
        return ResponseEntity.ok( carrierService.create( newCarrierDTO ) );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_TRANSPORT')")
    @PostMapping("/available/{carrierId}")
    public ResponseEntity<ResponseWrapperDTO<CarrierDTO>> create(
            @PathVariable String carrierId
    ){
        return ResponseEntity.ok( carrierService.availableStatus( carrierId ) );
    }
}
