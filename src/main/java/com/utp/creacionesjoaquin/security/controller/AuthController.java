package com.utp.creacionesjoaquin.security.controller;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.security.dto.*;
import com.utp.creacionesjoaquin.security.service.UserService;
import com.utp.creacionesjoaquin.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;


    @GetMapping("/getUserFromToken")
    public ResponseEntity<ResponseWrapperDTO<UserDTO>> getUserFromToken(@RequestHeader(name = "Authorization") String tokenHeader){
        System.out.println();
        String token = tokenHeader.length() > 7 ? tokenHeader.substring(7) : "no token";
        return ResponseEntity.ok( userService.getUserFromToken(token) );
    }

    @PostMapping("/roles")
    public ResponseEntity<ResponseWrapperDTO<String>> createRoles(){
        return ResponseEntity.ok( userService.createRoles() );
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapperDTO<JwtTokenDTO>> loginUser(@Valid @RequestBody LoginUserDTO loginUserDTO){
        return ResponseEntity.ok( userService.loginUser(loginUserDTO) );
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseWrapperDTO<JwtTokenDTO>> registerUser(@RequestBody NewUserDTO newUserDTO){
        return ResponseEntity.ok( userService.registerUser(newUserDTO) );
    }

    @GetMapping("/sendConfirmationEmail")
    public ResponseEntity<ResponseWrapperDTO<String>> confirmationEmail(@RequestParam(name = "to") String to){
        return ResponseEntity.ok( emailService.sendHtmlTemplateEmail(to) );
    }

    @PutMapping("/changePassword")
    public ResponseEntity<ResponseWrapperDTO<String>> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO){
        return ResponseEntity.ok( userService.changePassword(changePasswordDTO) );
    }


}
