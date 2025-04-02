package com.utp.creacionesjoaquin.security.dto;

import com.utp.creacionesjoaquin.profile.dto.address.AddressDTO;
import com.utp.creacionesjoaquin.warehouse.dto.carrier.CarrierDTO;
import com.utp.creacionesjoaquin.warehouse.dto.grocer.GrocerDTO;
import com.utp.creacionesjoaquin.profile.model.PersonalInformation;
import com.utp.creacionesjoaquin.security.enums.RolName;
import com.utp.creacionesjoaquin.security.enums.Status;
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
        String photoUrl,
        Status status,
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
                user.getPersonalInformation().getPhotoUrl() != null ? user.getPersonalInformation().getPhotoUrl() : "https://st3.depositphotos.com/6672868/13701/v/450/depositphotos_137014128-stock-illustration-user-profile-icon.jpg",
                user.getStatus(),
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
                user.getPersonalInformation().getPhotoUrl() != null ? user.getPersonalInformation().getPhotoUrl() : "https://st3.depositphotos.com/6672868/13701/v/450/depositphotos_137014128-stock-illustration-user-profile-icon.jpg",
                user.getStatus(),
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
                user.getPersonalInformation().getPhotoUrl() != null ? user.getPersonalInformation().getPhotoUrl() : "https://st3.depositphotos.com/6672868/13701/v/450/depositphotos_137014128-stock-illustration-user-profile-icon.jpg",
                user.getStatus(),
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
