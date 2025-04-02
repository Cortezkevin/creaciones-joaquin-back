package com.utp.creacionesjoaquin.profile.service;

import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.profile.dto.address.AddressDTO;
import com.utp.creacionesjoaquin.profile.dto.address.NewAddressDTO;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.profile.model.Address;
import com.utp.creacionesjoaquin.profile.model.PersonalInformation;
import com.utp.creacionesjoaquin.profile.repository.PersonalInformationRepository;
import com.utp.creacionesjoaquin.security.model.User;
import com.utp.creacionesjoaquin.security.repository.AddressRepository;
import com.utp.creacionesjoaquin.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final PersonalInformationRepository personalInformationRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public ResponseWrapperDTO<AddressDTO> create(NewAddressDTO newAddressDTO){
        try {
            User user = userRepository.findById(newAddressDTO.userId() ).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            PersonalInformation personalInformation = personalInformationRepository.findByUser( user ).orElseThrow(() -> new ResourceNotFoundException("Informacion no encontrada"));

            Address newAddress = Address.builder()
                    .lng(newAddressDTO.lng())
                    .lta(newAddressDTO.lta())
                    .department(newAddressDTO.department())
                    .district(newAddressDTO.district())
                    .province(newAddressDTO.province())
                    .street(newAddressDTO.street())
                    .postalCode(newAddressDTO.postalCode())
                    .urbanization(newAddressDTO.urbanization())
                    .fullAddress(newAddressDTO.fullAddress())
                    .build();

            Address addressCreated = addressRepository.save( newAddress );

            personalInformation.setAddress( addressCreated );

            PersonalInformation personalInformationUpdated = personalInformationRepository.save( personalInformation );

            return ResponseWrapperDTO.<AddressDTO>builder()
                    .message("Direccion creada")
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .content(AddressDTO.parseToDTO( addressCreated ))
                    .build();
        }catch ( ResourceNotFoundException e ){
            return ResponseWrapperDTO.<AddressDTO>builder()
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .message("Ocurrio un error: " + e.getMessage())
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<AddressDTO> updateAddress( AddressDTO addressDTO ){
        try {
            Address address = addressRepository.findById( addressDTO.id() ).orElseThrow(() -> new ResourceNotFoundException(("Direccion no encontrada")));

            address.setDepartment(addressDTO.department());
            address.setProvince(addressDTO.province());
            address.setDistrict(addressDTO.district());
            address.setUrbanization(addressDTO.urbanization());
            address.setStreet(addressDTO.street());
            address.setLng(addressDTO.lng());
            address.setLta(addressDTO.lta());
            address.setPostalCode(addressDTO.postalCode());
            address.setFullAddress(addressDTO.fullAddress());

            Address addressUpdated = addressRepository.save( address );

            return ResponseWrapperDTO.<AddressDTO>builder()
                    .success(true)
                    .content( AddressDTO.parseToDTO( addressUpdated ))
                    .status(HttpStatus.OK.name())
                    .message("Direccion actualizada")
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<AddressDTO>builder()
                    .success(false)
                    .content( null )
                    .status(HttpStatus.BAD_REQUEST.name())
                    .message("Ocurrio un error: " + e.getMessage())
                    .build();
        }
    }

    public ResponseWrapperDTO<AddressDTO> getAddressFromUser(String userId) {
        try {
            User user = userRepository.findById( userId ).orElseThrow(() -> new ResourceNotFoundException(("Usuario no encontrado")));
            PersonalInformation personalInformation = personalInformationRepository.findByUser( user ).orElseThrow(() -> new ResourceNotFoundException(("Informacion no encontrada")));
            Address address = addressRepository.findByPersonalInformation( personalInformation ).orElseThrow(() -> new ResourceNotFoundException(("Direccion no encontrada")));

            return ResponseWrapperDTO.<AddressDTO>builder()
                    .message("Solicitud satisfactoria")
                    .success(true)
                    .status(HttpStatus.OK.name())
                    .content(AddressDTO.parseToDTO( address ))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<AddressDTO>builder()
                    .success(false)
                    .content( null )
                    .status(HttpStatus.BAD_REQUEST.name())
                    .message("Ocurrio un error: " + e.getMessage())
                    .build();
        }
    }
}
