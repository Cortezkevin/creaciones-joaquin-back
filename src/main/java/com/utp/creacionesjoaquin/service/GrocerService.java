package com.utp.creacionesjoaquin.service;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.grocer.GrocerDTO;
import com.utp.creacionesjoaquin.dto.grocer.NewGrocerDTO;

import com.utp.creacionesjoaquin.enums.GrocerStatus;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.model.Grocer;
import com.utp.creacionesjoaquin.repository.GrocerRepository;
import com.utp.creacionesjoaquin.security.model.User;
import com.utp.creacionesjoaquin.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GrocerService {

    private final GrocerRepository grocerRepository;
    private final UserRepository userRepository;

    public ResponseWrapperDTO<List<GrocerDTO>> getAll(){
        List<GrocerDTO> grocers = grocerRepository.findAll().stream().map(GrocerDTO::parseToDTO).toList();
        return ResponseWrapperDTO.<List<GrocerDTO>>builder()
                .success(true)
                .message("Solicitud satisfactoria")
                .status(HttpStatus.OK.name())
                .content(grocers)
                .build();
    }

    public ResponseWrapperDTO<GrocerDTO> create(NewGrocerDTO newGrocerDTO){
        try {
            User user = userRepository.findById(newGrocerDTO.userId()).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            Grocer newGrocer = Grocer.builder()
                    .user(user)
                    .status(GrocerStatus.DISPONIBLE)
                    .build();

            Grocer grocerCreated = grocerRepository.save( newGrocer );

            return ResponseWrapperDTO.<GrocerDTO>builder()
                    .success(true)
                    .message("Almacenero Creado Satisfactoriamente")
                    .status(HttpStatus.OK.name())
                    .content(GrocerDTO.parseToDTO(grocerCreated))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<GrocerDTO>builder()
                    .success(false)
                    .message("Ocurrio un error: " + e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content(null)
                    .build();
        }
    }
}
