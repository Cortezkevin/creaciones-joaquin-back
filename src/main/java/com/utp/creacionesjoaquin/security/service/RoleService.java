package com.utp.creacionesjoaquin.security.service;

import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.commons.dto.RoleDTO;
import com.utp.creacionesjoaquin.security.enums.RolName;
import com.utp.creacionesjoaquin.security.model.Role;
import com.utp.creacionesjoaquin.security.repository.RoleRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository repository;

    public ResponseWrapperDTO<List<RoleDTO>> getRoles(){
        List<RoleDTO> roleDTOList = repository.findAll().stream().map(RoleDTO::parseToDTO).toList();
        return ResponseWrapperDTO.<List<RoleDTO>>builder()
                .message("Solicitud Satisfactoria")
                .status(HttpStatus.OK.name())
                .success(true)
                .content( roleDTOList )
                .build();
    }

    @SneakyThrows
    public Role getByRolName(RolName rolName){
        Role role = repository.findByRolName(rolName).orElse(null);
        if( role != null ){
            return role;
        }
        throw new Exception("Rol no encontrado con nombre: " + rolName.name());
    }

}
