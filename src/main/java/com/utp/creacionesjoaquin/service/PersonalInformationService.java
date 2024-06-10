package com.utp.creacionesjoaquin.service;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.information.PersonalInformationDTO;
import com.utp.creacionesjoaquin.dto.information.UpdatePersonalInformationDTO;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.model.PersonalInformation;
import com.utp.creacionesjoaquin.repository.PersonalInformationRepository;
import com.utp.creacionesjoaquin.security.model.User;
import com.utp.creacionesjoaquin.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalInformationService {

    private final UserRepository userRepository;
    private final PersonalInformationRepository personalInformationRepository;

    public ResponseWrapperDTO<PersonalInformationDTO> update(UpdatePersonalInformationDTO updatePersonalInformationDTO){
        try {
            User user = userRepository.findById( updatePersonalInformationDTO.userId()).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            PersonalInformation personalInformation = personalInformationRepository.findByUser( user ).orElseThrow(() -> new ResourceNotFoundException("Informacion no encontrada"));
            personalInformation.setPhone(updatePersonalInformationDTO.phone());
            personalInformation.setBirthdate(updatePersonalInformationDTO.birthdate());
            personalInformation.setFirstName(updatePersonalInformationDTO.firstName());
            personalInformation.setLastName(updatePersonalInformationDTO.lastName());
            PersonalInformation personalInformationUpdated = personalInformationRepository.save( personalInformation );
            return ResponseWrapperDTO.<PersonalInformationDTO>builder()
                    .success(true)
                    .status(HttpStatus.OK.name())
                    .message("Informacion actualizada")
                    .content(PersonalInformationDTO.parseToDTO(personalInformationUpdated))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<PersonalInformationDTO>builder()
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .message("Ocurrio un error: " + e.getMessage())
                    .content(null)
                    .build();
        }
    }

}
