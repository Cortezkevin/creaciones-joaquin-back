package com.utp.creacionesjoaquin.service;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.carrier.CarrierDTO;
import com.utp.creacionesjoaquin.dto.carrier.NewCarrierDTO;
import com.utp.creacionesjoaquin.enums.CarrierStatus;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.model.Carrier;
import com.utp.creacionesjoaquin.repository.CarrierRepository;
import com.utp.creacionesjoaquin.security.model.User;
import com.utp.creacionesjoaquin.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarrierService {

    private final CarrierRepository carrierRepository;
    private final UserRepository userRepository;

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

            Carrier newCarrier = Carrier.builder()
                    .user(user)
                    .codePlate(newCarrierDTO.plateCode())
                    .status(newCarrierDTO.status())
                    .build();

            Carrier carrierCreated = carrierRepository.save( newCarrier );

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
