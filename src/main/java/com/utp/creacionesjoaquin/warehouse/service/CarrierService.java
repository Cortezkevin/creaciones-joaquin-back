package com.utp.creacionesjoaquin.warehouse.service;

import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.warehouse.dto.carrier.CarrierDTO;
import com.utp.creacionesjoaquin.warehouse.dto.carrier.NewCarrierDTO;
import com.utp.creacionesjoaquin.commons.enums.CarrierStatus;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.warehouse.model.Carrier;
import com.utp.creacionesjoaquin.warehouse.repository.CarrierRepository;
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
public class CarrierService {

    private final CarrierRepository carrierRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public ResponseWrapperDTO<List<CarrierDTO>> getAll(){
        List<CarrierDTO> carriers = carrierRepository.findAll().stream().map(CarrierDTO::parseToDTO).toList();
        return ResponseWrapperDTO.<List<CarrierDTO>>builder()
                .success(true)
                .message("Solicitud satisfactoria")
                .status(HttpStatus.OK.name())
                .content(carriers)
                .build();
    }

    public ResponseWrapperDTO<CarrierDTO> availableStatus(String carrierId){
        try {
            Carrier carrier = carrierRepository.findById(carrierId).orElseThrow(() -> new ResourceNotFoundException("Repartidor no encontrado"));
            if( !carrier.getStatus().equals(CarrierStatus.EN_DESCANSO) ){
                return ResponseWrapperDTO.<CarrierDTO>builder()
                        .success(false)
                        .message("Debes estar en descanso para cambiar a disponible")
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content(null)
                        .build();
            }
            carrier.setStatus( CarrierStatus.DISPONIBLE );
            Carrier carrierUpdated = carrierRepository.save( carrier );
            return ResponseWrapperDTO.<CarrierDTO>builder()
                    .success(true)
                    .message("Estado actualizado")
                    .status(HttpStatus.OK.name())
                    .content(CarrierDTO.parseToDTO( carrierUpdated ))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<CarrierDTO>builder()
                    .success(false)
                    .message("Ocurrio un error: " + e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<CarrierDTO> create(NewCarrierDTO newCarrierDTO){
        try {
            User user = userRepository.findById(newCarrierDTO.userId()).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            Role role = roleRepository.findByRolName(RolName.ROLE_TRANSPORT).orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));
            Set<Role> roles = user.getRoles();
            roles.add( role );

            Carrier newCarrier = Carrier.builder()
                    .user(user)
                    .codePlate(newCarrierDTO.plateCode())
                    .status(newCarrierDTO.status())
                    .build();

            user.setRoles( roles );
            Carrier carrierCreated = carrierRepository.save( newCarrier );
            user.setCarrier( carrierCreated );
            userRepository.save( user );

            return ResponseWrapperDTO.<CarrierDTO>builder()
                    .success(true)
                    .message("Repartidor Creado Satisfactoriamente")
                    .status(HttpStatus.OK.name())
                    .content(CarrierDTO.parseToDTO(carrierCreated))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<CarrierDTO>builder()
                    .success(false)
                    .message("Ocurrio un error: " + e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content(null)
                    .build();
        }
    }
}
