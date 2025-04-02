package com.utp.creacionesjoaquin.security.service;

import com.utp.creacionesjoaquin.commons.utils.constants.ApiResponseMessages;
import com.utp.creacionesjoaquin.commons.utils.constants.ExceptionMessages;
import com.utp.creacionesjoaquin.config.cloudinary.service.CloudinaryService;
import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.profile.dto.user.UpdateUserDTO;
import com.utp.creacionesjoaquin.exception.customException.ResourceDuplicatedException;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.administration.model.Product;
import com.utp.creacionesjoaquin.sales.model.Cart;
import com.utp.creacionesjoaquin.sales.model.CartItem;
import com.utp.creacionesjoaquin.profile.model.Address;
import com.utp.creacionesjoaquin.profile.model.PersonalInformation;
import com.utp.creacionesjoaquin.sales.repository.CartItemRepository;
import com.utp.creacionesjoaquin.sales.repository.CartRepository;
import com.utp.creacionesjoaquin.profile.repository.PersonalInformationRepository;
import com.utp.creacionesjoaquin.administration.repository.ProductRepository;
import com.utp.creacionesjoaquin.security.dto.*;
import com.utp.creacionesjoaquin.security.enums.RolName;
import com.utp.creacionesjoaquin.security.enums.Status;
import com.utp.creacionesjoaquin.security.jwt.JwtProvider;
import com.utp.creacionesjoaquin.security.model.MainUser;
import com.utp.creacionesjoaquin.security.model.Role;
import com.utp.creacionesjoaquin.security.model.User;
import com.utp.creacionesjoaquin.security.repository.AddressRepository;
import com.utp.creacionesjoaquin.security.repository.RoleRepository;
import com.utp.creacionesjoaquin.security.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PersonalInformationRepository personalInformationRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartRepository cartRepository;


    public ResponseWrapperDTO<List<UserDTO>> findAllUsers(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MainUser mainUser = (MainUser) authentication.getPrincipal();
        List<UserDTO> userDTOList = userRepository.findAll().stream()
                .filter(u -> !u.getEmail().equals(mainUser.getEmail()))
                .map(UserDTO::parseToDTO).toList();
        return ResponseWrapperDTO.<List<UserDTO>>builder()
                .success(true)
                .message(ApiResponseMessages.SUCCESS)
                .status(HttpStatus.OK.name())
                .content( userDTOList )
                .build();
    }

    @SneakyThrows
    public User getByUsername(String username ) {
        User user = userRepository.findByEmail( username ).orElse(null);
        if( user != null ){
            return user;
        }
        throw new ResourceNotFoundException(ExceptionMessages.USER_NOT_FOUND);
    }

    public ResponseWrapperDTO<UserDTO> createUser(CreateUserDTO createUserDTO){
        try {
            if( userRepository.existsByEmail(createUserDTO.email()) ) throw new ResourceDuplicatedException(createUserDTO.email() + " ya tiene una cuenta asociada");

            Set<Role> roles = new HashSet<>();

            for(String r: createUserDTO.roles()){
                Role roleUser = roleRepository.findByRolName( RolName.valueOf( r ) ).orElseThrow(() -> new ResourceNotFoundException("El rol: " + r + " no existe"));
                roles.add( roleUser );
            }

            User newUser = User.builder()
                    .email(createUserDTO.email())
                    .password(passwordEncoder.encode(createUserDTO.password()))
                    .roles( roles )
                    .status(createUserDTO.status())
                    .build();

            User userCreated = userRepository.save(newUser);

            Cart newCart = Cart.createEmpty();
            newCart.setUser( userCreated );

            Cart cartCreated = cartRepository.save( newCart );

            Address addressCreated = addressRepository.save( Address.createEmpty() );

            PersonalInformation newPersonalInformation = PersonalInformation.builder()
                    .firstName(createUserDTO.firstName())
                    .lastName(createUserDTO.lastName())
                    .phone("")
                    .user(userCreated)
                    .address(addressCreated)
                    .build();

            PersonalInformation personalInformationCreated = personalInformationRepository.save( newPersonalInformation );

            userCreated.setPersonalInformation( personalInformationCreated );
            userCreated.setCart( cartCreated );

            User userRecent = userRepository.save( userCreated );

            return ResponseWrapperDTO.<UserDTO>builder()
                    .message("Usuario creado satisfactoriamente")
                    .status(HttpStatus.OK.name())
                    .success( true )
                    .content( UserDTO.parseToDTO( userRecent ) )
                    .build();
        }catch (Exception e){
            return ResponseWrapperDTO.<UserDTO>builder()
                    .message("Ocurrio un error: " + e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success( false )
                    .content( null )
                    .build();
        }
    }

    public ResponseWrapperDTO<UserDTO> update(UpdateUserDTO updateUserDTO) {
        try {
            User user = userRepository.findById( updateUserDTO.userId() ).orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.USER_NOT_FOUND));
            user.setEmail( updateUserDTO.email() );
            user.setStatus( updateUserDTO.status() );

            PersonalInformation personalInformation = personalInformationRepository.findByUser( user ).orElse(null);
            if( personalInformation != null ){
                personalInformation.setFirstName(updateUserDTO.firstName());
                personalInformation.setLastName(updateUserDTO.lastName());
                PersonalInformation personalInformationUpdated = personalInformationRepository.save(personalInformation);
                user.setPersonalInformation( personalInformationUpdated );
            }

            Set<Role> roles = new HashSet<>();

            for(String r: updateUserDTO.roles()){
                Role roleUser = roleRepository.findByRolName( RolName.valueOf( r ) ).orElseThrow(() -> new ResourceNotFoundException("El rol: " + r + " no existe"));
                roles.add( roleUser );
            }

            user.setRoles( roles );

            User userUpdated = userRepository.save( user );

            return ResponseWrapperDTO.<UserDTO>builder()
                    .message(ApiResponseMessages.USER_UPDATED)
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