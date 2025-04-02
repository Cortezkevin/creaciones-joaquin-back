package com.utp.creacionesjoaquin.security.service;

import com.utp.creacionesjoaquin.administration.model.Product;
import com.utp.creacionesjoaquin.administration.repository.ProductRepository;
import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.commons.utils.constants.ApiResponseMessages;
import com.utp.creacionesjoaquin.commons.utils.constants.ExceptionMessages;
import com.utp.creacionesjoaquin.config.cloudinary.service.CloudinaryService;
import com.utp.creacionesjoaquin.exception.customException.ResourceDuplicatedException;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.profile.model.Address;
import com.utp.creacionesjoaquin.profile.model.PersonalInformation;
import com.utp.creacionesjoaquin.profile.repository.PersonalInformationRepository;
import com.utp.creacionesjoaquin.sales.model.Cart;
import com.utp.creacionesjoaquin.sales.model.CartItem;
import com.utp.creacionesjoaquin.sales.repository.CartItemRepository;
import com.utp.creacionesjoaquin.sales.repository.CartRepository;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

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


    public ResponseWrapperDTO<String> createRoles(){
        roleRepository.save(Role.builder().rolName(RolName.ROLE_USER).build());
        roleRepository.save(Role.builder().rolName(RolName.ROLE_ADMIN).build());
        roleRepository.save(Role.builder().rolName(RolName.ROLE_TRANSPORT).build());
        roleRepository.save(Role.builder().rolName(RolName.ROLE_WAREHOUSE).build());
        return ResponseWrapperDTO.<String>builder()
                .message(ApiResponseMessages.SUCCESS)
                .content(ApiResponseMessages.ROLE_CREATED)
                .success(true)
                .status(HttpStatus.OK.name())
                .build();
    }


    @SneakyThrows
    public ResponseWrapperDTO<UserDTO> getUserFromToken(String token ){
        String username = jwtProvider.getUsernameFromToken( token );
        User user = userRepository.findByEmail( username ).orElseThrow( () -> new ResourceNotFoundException(ExceptionMessages.USER_NOT_FOUND)) ;
        if( user.getStatus().equals(Status.INACTIVO)){
            return ResponseWrapperDTO.<UserDTO>builder()
                    .message(ApiResponseMessages.USER_DISABLED)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success( false )
                    .content( null )
                    .build();
        }
        return ResponseWrapperDTO.<UserDTO>builder()
                .message(ApiResponseMessages.SUCCESS)
                .status(HttpStatus.OK.name())
                .success( true )
                .content( UserDTO.parseToDTO( user ) )
                .build();
    }

    @SneakyThrows
    public ResponseWrapperDTO<JwtTokenDTO> loginUser(LoginUserDTO loginUserDTO ){
        User userFound = userRepository.findByEmail(loginUserDTO.email()).orElseThrow(() -> new ResourceNotFoundException("Email no existente"));
        if( userFound != null ){

            if( userFound.getStatus().equals(Status.INACTIVO)){
                return ResponseWrapperDTO.<JwtTokenDTO>builder()
                        .message(ApiResponseMessages.USER_DISABLED)
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

    @SneakyThrows
    public ResponseWrapperDTO<JwtTokenDTO> registerUser(NewUserDTO newUserDTO ){
        try {
            if( userRepository.existsByEmail(newUserDTO.email()) ) throw new ResourceDuplicatedException(newUserDTO.email() + " ya tiene una cuenta asociada");

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
            User userCreated = userRepository.save(newUser);


            Cart newCart = Cart.createEmpty();
            newCart.setUser( userCreated );

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

            Address newAddress = Address.createEmpty();

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
            User userRecent = userRepository.save( userCreated );


            MainUser mainUser = MainUser.build( userRecent );
            String token = jwtProvider.generateToken( mainUser );

            JwtTokenDTO jwtTokenDTO = new JwtTokenDTO(
                    token,
                    UserDTO.parseToDTO( userRecent, personalInformationCreated )
            );
            return ResponseWrapperDTO.<JwtTokenDTO>builder()
                    .message(ApiResponseMessages.USER_CREATED)
                    .status(HttpStatus.OK.name())
                    .success( true )
                    .content( jwtTokenDTO )
                    .build();
        }catch (Exception e){
            return ResponseWrapperDTO.<JwtTokenDTO>builder()
                    .message(ApiResponseMessages.ERROR + ": " + e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success( false )
                    .content( null )
                    .build();
        }
    }

    public ResponseWrapperDTO<String> changePassword(ChangePasswordDTO dto){
        try {
            User user = userRepository.findByTokenPassword( dto.tokenPassword() ).orElseThrow(() -> new ResourceNotFoundException("El token ingresado no es valido"));
            if( dto.password().equals(dto.confirmPassword())){
                user.setPassword(passwordEncoder.encode( dto.password()));
                user.setTokenPassword(null);
                userRepository.save( user );
                return ResponseWrapperDTO.<String>builder()
                        .message(ApiResponseMessages.USER_PASSWORD_CHANGED)
                        .status(HttpStatus.OK.name())
                        .success( true )
                        .content("Se actualizo su contrasena")
                        .build();
            }else {
                return ResponseWrapperDTO.<String>builder()
                        .message(ApiResponseMessages.USER_PASSWORDS_DOES_NOT_MATCH)
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
}
