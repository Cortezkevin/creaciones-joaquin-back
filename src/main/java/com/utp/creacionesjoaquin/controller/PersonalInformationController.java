package com.utp.creacionesjoaquin.controller;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.information.PersonalInformationDTO;
import com.utp.creacionesjoaquin.dto.information.UpdatePersonalInformationDTO;
import com.utp.creacionesjoaquin.service.PersonalInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class PersonalInformationController {
    private final PersonalInformationService personalInformationService;

    @PutMapping
    public ResponseEntity<ResponseWrapperDTO<PersonalInformationDTO>> update(
            @RequestBody UpdatePersonalInformationDTO updatePersonalInformationDTO
            ){
        return ResponseEntity.ok( personalInformationService.update( updatePersonalInformationDTO) );
    }
}
