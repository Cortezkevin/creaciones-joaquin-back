package com.utp.creacionesjoaquin.controller;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.address.AddressDTO;
import com.utp.creacionesjoaquin.dto.address.NewAddressDTO;
import com.utp.creacionesjoaquin.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<ResponseWrapperDTO<AddressDTO>> create(
            @RequestBody NewAddressDTO newAddressDTO
    ){
        return ResponseEntity.ok( addressService.create( newAddressDTO ) );
    }


    @PutMapping
    public ResponseEntity<ResponseWrapperDTO<AddressDTO>> updateAddress(
            @RequestBody AddressDTO addressDTO
    ){
        return ResponseEntity.ok( addressService.updateAddress( addressDTO ) );
    }

}
