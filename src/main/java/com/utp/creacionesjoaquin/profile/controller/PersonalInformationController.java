package com.utp.creacionesjoaquin.profile.controller;

import com.utp.creacionesjoaquin.config.cloudinary.utils.UploadUtils;
import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.profile.dto.user.UpdateProfile;
import com.utp.creacionesjoaquin.security.dto.UserDTO;
import com.utp.creacionesjoaquin.profile.service.PersonalInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@CrossOrigin
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class PersonalInformationController {
    private final PersonalInformationService personalInformationService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PutMapping( consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResponseWrapperDTO<UserDTO>> update(
            @RequestPart(name = "file", required = false) MultipartFile multipartFile,
            @RequestPart("updateProfileDTO") String updateProfileDTOString
    ){
        UpdateProfile updateProfile = UploadUtils.convertStringToObject( updateProfileDTOString, UpdateProfile.class );
        File fileToUpload = UploadUtils.getFileFromMultipartFile( multipartFile );
        return ResponseEntity.ok( personalInformationService.updateProfile( updateProfile, fileToUpload ) );
    }
}
