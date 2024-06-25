package com.utp.creacionesjoaquin.service;

import com.utp.creacionesjoaquin.cloudinary.dto.UploadDTO;
import com.utp.creacionesjoaquin.cloudinary.service.CloudinaryService;
import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.information.PersonalInformationDTO;
import com.utp.creacionesjoaquin.dto.information.UpdatePersonalInformationDTO;
import com.utp.creacionesjoaquin.dto.product.UploadResultDTO;
import com.utp.creacionesjoaquin.dto.user.UpdateProfile;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.model.PersonalInformation;
import com.utp.creacionesjoaquin.repository.PersonalInformationRepository;
import com.utp.creacionesjoaquin.security.dto.UserDTO;
import com.utp.creacionesjoaquin.security.model.User;
import com.utp.creacionesjoaquin.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PersonalInformationService {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final PersonalInformationRepository personalInformationRepository;

    public ResponseWrapperDTO<UserDTO> updateProfile(UpdateProfile updateProfile, File file) {
        try {
            User user = userRepository.findById( updateProfile.userId() ).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            PersonalInformation personalInformation = personalInformationRepository.findByUser( user ).orElse(null);
            if( personalInformation != null ){

                if( file != null){
                    UploadDTO uploadDTO = new UploadDTO(file, user.getId());
                    UploadResultDTO uploadResultDTO = cloudinaryService.upload( "profile", uploadDTO );
                    if( uploadResultDTO != null){
                        personalInformation.setPhotoUrl( uploadResultDTO.url() );
                    }
                }

                user.setEmail(updateProfile.email() );
                personalInformation.setFirstName(updateProfile.firstName());
                personalInformation.setLastName(updateProfile.lastName());
                personalInformation.setPhone(updateProfile.phone());
                if( updateProfile.birthdate() != null && !updateProfile.birthdate().equals("")){
                    personalInformation.setBirthdate(LocalDate.parse(updateProfile.birthdate()));
                }
                PersonalInformation personalInformationUpdated = personalInformationRepository.save(personalInformation);
                user.setPersonalInformation( personalInformationUpdated );
            }

            User userUpdated = userRepository.save( user );

            return ResponseWrapperDTO.<UserDTO>builder()
                    .message("Se actualizo su informacion")
                    .status(HttpStatus.OK.name())
                    .success( true )
                    .content(UserDTO.parseToDTO(userUpdated))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<UserDTO>builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success( false )
                    .content( null )
                    .build();
        }
    }

}
