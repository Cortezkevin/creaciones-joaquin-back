package com.utp.creacionesjoaquin.profile.controller;

import com.utp.creacionesjoaquin.config.cloudinary.utils.UploadUtils;
import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.commons.dto.RoleDTO;
import com.utp.creacionesjoaquin.profile.dto.user.UpdateProfile;
import com.utp.creacionesjoaquin.profile.dto.user.UpdateUserDTO;
import com.utp.creacionesjoaquin.profile.service.PersonalInformationService;
import com.utp.creacionesjoaquin.security.dto.CreateUserDTO;
import com.utp.creacionesjoaquin.security.dto.UserDTO;
import com.utp.creacionesjoaquin.security.service.RoleService;
import com.utp.creacionesjoaquin.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final PersonalInformationService personalInformationService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<ResponseWrapperDTO<List<UserDTO>>> getAll(){
        return ResponseEntity.ok( userService.findAllUsers() );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/roles")
    public ResponseEntity<ResponseWrapperDTO<List<RoleDTO>>> getAllRoles(){
        return ResponseEntity.ok( roleService.getRoles() );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseWrapperDTO<UserDTO>> create(
            @Valid @RequestBody CreateUserDTO createUserDTO
            ){
        return ResponseEntity.ok( userService.createUser( createUserDTO ) );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<ResponseWrapperDTO<UserDTO>> update(
            @RequestBody UpdateUserDTO updateUserDTO
            ){
        return ResponseEntity.ok( userService.update( updateUserDTO ) );
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping(value = "/profile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResponseWrapperDTO<UserDTO>> update(
            @RequestPart(name = "file", required = false) MultipartFile multipartFile,
            @RequestPart("updateProfileDTO") String updateProfileDTOString
    ){
        UpdateProfile updateProfile = UploadUtils.convertStringToObject( updateProfileDTOString, UpdateProfile.class );
        File fileToUpload = UploadUtils.getFileFromMultipartFile( multipartFile );
        return ResponseEntity.ok( personalInformationService.updateProfile( updateProfile, fileToUpload ) );
    }
}
