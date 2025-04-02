package com.utp.creacionesjoaquin.security.controller;

import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.security.dto.*;
import com.utp.creacionesjoaquin.security.service.AuthService;
import com.utp.creacionesjoaquin.profile.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;


    @GetMapping("/getUserFromToken")
    public ResponseEntity<ResponseWrapperDTO<UserDTO>> getUserFromToken(@RequestHeader(name = "Authorization") String tokenHeader){
        System.out.println();
        String token = tokenHeader.length() > 7 ? tokenHeader.substring(7) : "no token";
        return ResponseEntity.ok( authService.getUserFromToken(token) );
    }

    @PostMapping("/roles")
    public ResponseEntity<ResponseWrapperDTO<String>> createRoles(){
        return ResponseEntity.ok( authService.createRoles() );
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapperDTO<JwtTokenDTO>> loginUser(@Valid @RequestBody LoginUserDTO loginUserDTO){
        return ResponseEntity.ok( authService.loginUser(loginUserDTO) );
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseWrapperDTO<JwtTokenDTO>> registerUser(@RequestBody NewUserDTO newUserDTO){
        return ResponseEntity.ok( authService.registerUser(newUserDTO) );
    }

    @GetMapping("/sendConfirmationEmail")
    public ResponseEntity<ResponseWrapperDTO<String>> confirmationEmail(@RequestParam(name = "to") String to){
        return ResponseEntity.ok( emailService.sendHtmlTemplateEmail(to) );
    }

    @PutMapping("/changePassword")
    public ResponseEntity<ResponseWrapperDTO<String>> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO){
        return ResponseEntity.ok( authService.changePassword(changePasswordDTO) );
    }


}
