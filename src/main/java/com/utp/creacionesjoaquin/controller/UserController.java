package com.utp.creacionesjoaquin.controller;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.RoleDTO;
import com.utp.creacionesjoaquin.dto.user.UpdateUserDTO;
import com.utp.creacionesjoaquin.security.dto.UserDTO;
import com.utp.creacionesjoaquin.security.service.RoleService;
import com.utp.creacionesjoaquin.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

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
    @PutMapping
    public ResponseEntity<ResponseWrapperDTO<UserDTO>> update(
            @RequestBody UpdateUserDTO updateUserDTO
            ){
        return ResponseEntity.ok( userService.update( updateUserDTO ) );
    }
}
