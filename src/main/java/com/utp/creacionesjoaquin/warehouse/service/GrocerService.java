package com.utp.creacionesjoaquin.warehouse.service;

import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.warehouse.dto.grocer.GrocerDTO;
import com.utp.creacionesjoaquin.warehouse.dto.grocer.NewGrocerDTO;

import com.utp.creacionesjoaquin.commons.enums.GrocerStatus;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.warehouse.model.Grocer;
import com.utp.creacionesjoaquin.warehouse.repository.GrocerRepository;
import com.utp.creacionesjoaquin.security.enums.RolName;
import com.utp.creacionesjoaquin.security.model.Role;
import com.utp.creacionesjoaquin.security.model.User;
import com.utp.creacionesjoaquin.security.repository.RoleRepository;
import com.utp.creacionesjoaquin.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GrocerService {

    private final GrocerRepository grocerRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

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
            Role role = roleRepository.findByRolName(RolName.ROLE_WAREHOUSE).orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));
            Set<Role> roles = user.getRoles();
            roles.add( role );

            Grocer newGrocer = Grocer.builder()
                    .user(user)
                    .status(GrocerStatus.DISPONIBLE)
                    .build();

            user.setRoles( roles );
            Grocer grocerCreated = grocerRepository.save( newGrocer );
            user.setGrocer( grocerCreated );
            userRepository.save( user );

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
