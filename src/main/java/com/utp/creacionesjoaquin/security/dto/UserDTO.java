package com.utp.creacionesjoaquin.security.dto;

import com.utp.creacionesjoaquin.dto.address.AddressDTO;
import com.utp.creacionesjoaquin.dto.carrier.CarrierDTO;
import com.utp.creacionesjoaquin.dto.grocer.GrocerDTO;
import com.utp.creacionesjoaquin.model.PersonalInformation;
import com.utp.creacionesjoaquin.security.enums.RolName;
import com.utp.creacionesjoaquin.security.model.MainUser;
import com.utp.creacionesjoaquin.security.model.Role;
import com.utp.creacionesjoaquin.security.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public record UserDTO (
        String id,
        String firstName,
        String lastName,
        String email,
        Set<String> roles,
        ProfileDTO profile,
        Object roleExtraData
){

    public static UserDTO parseToDTO( User user, MainUser muser){
        return new UserDTO(
                user.getId(),
                user.getPersonalInformation().getFirstName(),
                user.getPersonalInformation().getLastName(),
                muser.getEmail(),
                muser.getRoles().stream().map(RolName::name).collect(Collectors.toSet()),
                new ProfileDTO(
                        user.getPersonalInformation().getBirthdate() != null ? user.getPersonalInformation().getBirthdate().toString() : "",
                        user.getPersonalInformation().getAddress() != null ? AddressDTO.parseToDTO( user.getPersonalInformation().getAddress() ) : null,
                        user.getPersonalInformation().getPhone() != null ? user.getPersonalInformation().getPhone() : ""
                ),
                user.getGrocer() != null ? GrocerDTO.parseToDTO(user.getGrocer()) : user.getCarrier() != null ? CarrierDTO.parseToDTO(user.getCarrier()) : null
        );
    }

    public static UserDTO parseToDTO(User user, PersonalInformation personalInformation){
        return new UserDTO(
                user.getId(),
                personalInformation.getFirstName(),
                personalInformation.getLastName(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getRolName).map(RolName::name).collect(Collectors.toSet()),
                new ProfileDTO(
                        user.getPersonalInformation().getBirthdate() != null ? user.getPersonalInformation().getBirthdate().toString() : "",
                        user.getPersonalInformation().getAddress() != null ? AddressDTO.parseToDTO( user.getPersonalInformation().getAddress() ) : null,
                        user.getPersonalInformation().getPhone() != null ? user.getPersonalInformation().getPhone() : ""
                ),
                user.getGrocer() != null ? GrocerDTO.parseToDTO(user.getGrocer()) : user.getCarrier() != null ? CarrierDTO.parseToDTO(user.getCarrier()) : null
        );
    }

    public static UserDTO parseToDTO( User user){
        return new UserDTO(
                user.getId(),
                user.getPersonalInformation().getFirstName(),
                user.getPersonalInformation().getLastName(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getRolName).map(RolName::name).collect(Collectors.toSet()),
                new ProfileDTO(
                        user.getPersonalInformation().getBirthdate() != null ? user.getPersonalInformation().getBirthdate().toString() : "",
                        user.getPersonalInformation().getAddress() != null ? AddressDTO.parseToDTO( user.getPersonalInformation().getAddress() ) : null,
                        user.getPersonalInformation().getPhone() != null ? user.getPersonalInformation().getPhone() : ""
                ),
                user.getGrocer() != null ? GrocerDTO.parseToDTO(user.getGrocer()) : user.getCarrier() != null ? CarrierDTO.parseToDTO(user.getCarrier()) : null
        );
    }
}
