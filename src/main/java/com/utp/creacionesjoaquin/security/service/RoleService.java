package com.utp.creacionesjoaquin.security.service;

import com.utp.creacionesjoaquin.security.enums.RolName;
import com.utp.creacionesjoaquin.security.model.Role;
import com.utp.creacionesjoaquin.security.repository.RoleRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository repository;

    @SneakyThrows
    public Role getByRolName(RolName rolName){
        Role role = repository.findByRolName(rolName).orElse(null);
        if( role != null ){
            return role;
        }
        throw new Exception("Rol no encontrado con nombre: " + rolName.name());
    }

}
