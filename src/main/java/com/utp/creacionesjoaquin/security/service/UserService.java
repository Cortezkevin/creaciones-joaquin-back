package com.utp.creacionesjoaquin.security.service;

import com.utp.creacionesjoaquin.cloudinary.dto.UploadDTO;
import com.utp.creacionesjoaquin.cloudinary.service.CloudinaryService;
import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.product.UploadResultDTO;
import com.utp.creacionesjoaquin.dto.user.UpdateProfile;
import com.utp.creacionesjoaquin.dto.user.UpdateUserDTO;
import com.utp.creacionesjoaquin.exception.customException.ResourceDuplicatedException;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.model.*;
import com.utp.creacionesjoaquin.repository.CartItemRepository;
import com.utp.creacionesjoaquin.repository.CartRepository;
import com.utp.creacionesjoaquin.repository.PersonalInformationRepository;
import com.utp.creacionesjoaquin.repository.ProductRepository;
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

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PersonalInformationRepository personalInformationRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public ResponseWrapperDTO<List<UserDTO>> findAllUsers(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MainUser mainUser = (MainUser) authentication.getPrincipal();
        List<UserDTO> userDTOList = repository.findAll().stream()
                .filter(u -> !u.getEmail().equals(mainUser.getEmail()))
                .map(UserDTO::parseToDTO).toList();
        return ResponseWrapperDTO.<List<UserDTO>>builder()
                .success(true)
                .message("Solicitud satisfactoria")
                .status(HttpStatus.OK.name())
                .content( userDTOList )
                .build();
    }

    @SneakyThrows
    public User getByUsername(String username ) {
        User user = repository.findByEmail( username ).orElse(null);
        if( user != null ){
            return user;
        }
        throw new ResourceNotFoundException("User not found with username: " + username);
    }

    public ResponseWrapperDTO<String> createRoles(){
        roleRepository.save(Role.builder().rolName(RolName.ROLE_USER).build());
        roleRepository.save(Role.builder().rolName(RolName.ROLE_ADMIN).build());
        roleRepository.save(Role.builder().rolName(RolName.ROLE_TRANSPORT).build());
        roleRepository.save(Role.builder().rolName(RolName.ROLE_WAREHOUSE).build());
        return ResponseWrapperDTO.<String>builder()
                .message("Roles registrados")
                .content("Roles registrados en la base de datos")
                .success(true)
                .status(HttpStatus.OK.name())
                .build();
    }


    @SneakyThrows
    public ResponseWrapperDTO<UserDTO> getUserFromToken(String token ){
        String username = jwtProvider.getUsernameFromToken( token );
        User user = repository.findByEmail( username ).orElseThrow( () -> new ResourceNotFoundException("User not found with username: " + username)) ;
        if( user.getStatus().equals(Status.INACTIVO)){
            return ResponseWrapperDTO.<UserDTO>builder()
                    .message("Su cuenta fue deshabilitada, favor de comunicarse con soporte para mas informacion")
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success( false )
                    .content( null )
                    .build();
        }
        return ResponseWrapperDTO.<UserDTO>builder()
                .message("Success Request")
                .status(HttpStatus.OK.name())
                .success( true )
                .content( UserDTO.parseToDTO( user ) )
                .build();
    }

    @SneakyThrows
    public ResponseWrapperDTO<JwtTokenDTO> loginUser(LoginUserDTO loginUserDTO ){
        User userFound = repository.findByEmail(loginUserDTO.email()).orElseThrow(() -> new ResourceNotFoundException("Email no existente"));
        if( userFound != null ){

            if( userFound.getStatus().equals(Status.INACTIVO)){
                return ResponseWrapperDTO.<JwtTokenDTO>builder()
                        .message("Su cuenta fue deshabilitada, favor de comunicarse con soporte para mas informacion")
                        .status(HttpStatus.BAD_REQUEST.name())
                        .success( false )
                        .content( null )
                        .build();
            }

            boolean validPassword = passwordEncoder.matches(loginUserDTO.password(), userFound.getPassword());
            if( validPassword ){
                Authentication authentication =
                        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userFound.getEmail(), loginUserDTO.password()));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                MainUser mainUser = MainUser.build( userFound );

                String token = jwtProvider.generateToken( mainUser );
                JwtTokenDTO jwtTokenDTO = new JwtTokenDTO(
                        token,
                        UserDTO.parseToDTO( userFound , mainUser )
                );

                return ResponseWrapperDTO.<JwtTokenDTO>builder()
                        .message("Sesion iniciada correctamente")
                        .status(HttpStatus.OK.name())
                        .success( true )
                        .content( jwtTokenDTO )
                        .build();
            }
            throw new ResourceNotFoundException("Credenciales invalidas");
        }
        return null;
    }

    public ResponseWrapperDTO<UserDTO> createUser(CreateUserDTO createUserDTO){
        try {
            if( repository.existsByEmail(createUserDTO.email()) ) throw new ResourceDuplicatedException(createUserDTO.email() + " ya tiene una cuenta asociada");

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

            User userCreated = repository.save(newUser);

            Cart newCart = Cart.builder()
                    .user(userCreated)
                    .subtotal(BigDecimal.ZERO)
                    .total(BigDecimal.ZERO)
                    .shippingCost(BigDecimal.ZERO)
                    .tax(BigDecimal.ZERO)
                    .discount(BigDecimal.ZERO)
                    .build();

            Cart cartCreated = cartRepository.save( newCart );

            Address newAddress = Address.builder()
                    .urbanization("")
                    .postalCode(0)
                    .street("")
                    .fullAddress("")
                    .province("")
                    .district("")
                    .department("")
                    .build();

            Address addressCreated = addressRepository.save( newAddress );

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

            User userRecent = repository.save( userCreated );

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

    @SneakyThrows
    public ResponseWrapperDTO<JwtTokenDTO> registerUser(NewUserDTO newUserDTO ){
        try {
            if( repository.existsByEmail(newUserDTO.email()) ) throw new ResourceDuplicatedException(newUserDTO.email() + " ya tiene una cuenta asociada");

            Role roleAdmin = roleRepository.findByRolName( RolName.ROLE_ADMIN ).orElseThrow(() -> new ResourceNotFoundException(("Rol admin no existe")));
            Role roleUser = roleRepository.findByRolName( RolName.ROLE_USER ).orElseThrow(() -> new ResourceNotFoundException(("Rol user no existe")));

            Set<Role> roles = new HashSet<>();
            roles.add( roleUser );

            if( newUserDTO.isAdmin() != null && newUserDTO.isAdmin() ) {
                roles.add( roleAdmin );
            }

            User newUser = User.builder()
                    .email(newUserDTO.email())
                    .password(passwordEncoder.encode(newUserDTO.password()))
                    .roles( roles )
                    .status(Status.ACTIVO)
                    .build();
            User userCreated = repository.save(newUser);


            Cart newCart = Cart.builder()
                    .user(userCreated)
                    .subtotal(BigDecimal.ZERO)
                    .total(BigDecimal.ZERO)
                    .shippingCost(BigDecimal.ZERO)
                    .tax(BigDecimal.ZERO)
                    .discount(BigDecimal.ZERO)
                    .build();

            Cart cartCreated = cartRepository.save( newCart );


            if(newUserDTO.memoryCart() != null){
                List<CartItem> cartItemList = new ArrayList<>();
                newUserDTO.memoryCart().itemList().forEach( i -> {
                    Product product = productRepository.findById( i.productId() ).orElse(null);
                    if( product != null ){
                        CartItem cartItem = CartItem.builder()
                                .product( product )
                                .cart(cartCreated)
                                .total( product.getPrice().multiply( BigDecimal.valueOf(i.amount()) ) )
                                .amount(i.amount())
                                .build();
                        cartItemList.add( cartItem );
                    }
                });

                List<CartItem> cartItems = cartItemRepository.saveAll( cartItemList );
                cartCreated.setCartItems( cartItems );
                cartCreated.setShippingCost( newUserDTO.memoryCart().shippingCost() );
                cartRepository.save(cartCreated);

            }

            Cart cartRecent = cartRepository.findById( cartCreated.getId() ).orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado"));

            cartRecent.calculateTotals();

            cartRepository.save(cartRecent);

            Address newAddress = Address.builder()
                    .urbanization("")
                    .postalCode(0)
                    .street("")
                    .fullAddress("")
                    .province("")
                    .district("")
                    .department("")
                    .build();

            if(newUserDTO.memoryAddress() != null){
                newAddress.setFullAddress( newUserDTO.memoryAddress().fullAddress() );
                newAddress.setProvince( newUserDTO.memoryAddress().province() );
                newAddress.setDistrict( newUserDTO.memoryAddress().district() );
                newAddress.setDepartment( newUserDTO.memoryAddress().department() );
                newAddress.setStreet( newUserDTO.memoryAddress().street() );
                newAddress.setUrbanization( newUserDTO.memoryAddress().urbanization() );
                newAddress.setPostalCode( newUserDTO.memoryAddress().postalCode() );
                newAddress.setLng( newUserDTO.memoryAddress().lng() );
                newAddress.setLta( newUserDTO.memoryAddress().lta() );
            }

            Address addressCreated = addressRepository.save( newAddress );

            PersonalInformation newPersonalInformation = PersonalInformation.builder()
                    .firstName(newUserDTO.firstName())
                    .lastName(newUserDTO.lastName())
                    .phone("")
                    .user(userCreated)
                    .address(addressCreated)
                    .build();

            PersonalInformation personalInformationCreated = personalInformationRepository.save( newPersonalInformation );

            userCreated.setPersonalInformation( personalInformationCreated );
            User userRecent = repository.save( userCreated );


            MainUser mainUser = MainUser.build( userRecent );
            String token = jwtProvider.generateToken( mainUser );

            JwtTokenDTO jwtTokenDTO = new JwtTokenDTO(
                    token,
                    UserDTO.parseToDTO( userRecent, personalInformationCreated )
            );
            return ResponseWrapperDTO.<JwtTokenDTO>builder()
                .message("Usuario creado satisfactoriamente")
                .status(HttpStatus.OK.name())
                .success( true )
                .content( jwtTokenDTO )
                .build();
        }catch (Exception e){
            return ResponseWrapperDTO.<JwtTokenDTO>builder()
                    .message("Ocurrio un error: " + e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success( false )
                    .content( null )
                    .build();
        }
    }

    public ResponseWrapperDTO<String> changePassword(ChangePasswordDTO dto){
        try {
            User user = repository.findByTokenPassword( dto.tokenPassword() ).orElseThrow(() -> new ResourceNotFoundException("El token ingresado no es valido"));
            if( dto.password().equals(dto.confirmPassword())){
                user.setPassword(passwordEncoder.encode( dto.password()));
                user.setTokenPassword(null);
                repository.save( user );
                return ResponseWrapperDTO.<String>builder()
                        .message("Se actualizo su contrasena")
                        .status(HttpStatus.OK.name())
                        .success( true )
                        .content("Se actualizo su contrasena")
                        .build();
            }else {
                return ResponseWrapperDTO.<String>builder()
                        .message("Las contrasenas no coinciden")
                        .status(HttpStatus.BAD_REQUEST.name())
                        .success( false )
                        .content( null )
                        .build();
            }
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<String>builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success( false )
                    .content( null )
                    .build();
        }
    }

    public ResponseWrapperDTO<UserDTO> update(UpdateUserDTO updateUserDTO) {
        try {
            User user = repository.findById( updateUserDTO.userId() ).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
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

            User userUpdated = repository.save( user );

            return ResponseWrapperDTO.<UserDTO>builder()
                    .message("Se actualizo el usuario")
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

    public ResponseWrapperDTO<UserDTO> updateProfile(UpdateProfile updateProfile, File file) {
        try {
            User user = repository.findById( updateProfile.userId() ).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            PersonalInformation personalInformation = personalInformationRepository.findByUser( user ).orElse(null);
            if( personalInformation != null ){

                if( file != null){
                    UploadDTO uploadDTO = new UploadDTO(file, user.getId());
                    UploadResultDTO uploadResultDTO = cloudinaryService.upload( "profile", uploadDTO );
                    if( uploadResultDTO != null){
                        personalInformation.setPhotoUrl( uploadResultDTO.url() );
                    }
                }

                personalInformation.setFirstName(updateProfile.firstName());
                personalInformation.setLastName(updateProfile.lastName());
                personalInformation.setPhone(updateProfile.phone());
                personalInformation.setBirthdate(LocalDate.parse(updateProfile.birthdate()));
                PersonalInformation personalInformationUpdated = personalInformationRepository.save(personalInformation);
                user.setPersonalInformation( personalInformationUpdated );
            }

            User userUpdated = repository.save( user );

            return ResponseWrapperDTO.<UserDTO>builder()
                    .message("Se actualizo su informacion")
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
