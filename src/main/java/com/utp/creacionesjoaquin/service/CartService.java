package com.utp.creacionesjoaquin.service;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.cart.AddItemDTO;
import com.utp.creacionesjoaquin.dto.cart.CartDTO;
import com.utp.creacionesjoaquin.dto.cart.RemoveItemDTO;
import com.utp.creacionesjoaquin.dto.cart.UpdateShippingCostDTO;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.model.Cart;
import com.utp.creacionesjoaquin.model.CartItem;
import com.utp.creacionesjoaquin.model.Product;
import com.utp.creacionesjoaquin.repository.CartItemRepository;
import com.utp.creacionesjoaquin.repository.CartRepository;
import com.utp.creacionesjoaquin.repository.ProductRepository;
import com.utp.creacionesjoaquin.security.model.MainUser;
import com.utp.creacionesjoaquin.security.model.User;
import com.utp.creacionesjoaquin.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ResponseWrapperDTO<CartDTO> getCartFromUser(String userId){
         try {
             Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
             MainUser mainUser = (MainUser) authentication.getPrincipal();

             User userFounded = userRepository.findById( userId ).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));;
             User userSession = userRepository.findByEmail( mainUser.getEmail() ).orElseThrow(() -> new ResourceNotFoundException("No tiene autorizacion para ver este carrito"));

             if( !(userFounded.getEmail().equals(userSession.getEmail()))){
                 return ResponseWrapperDTO.<CartDTO>builder()
                         .message("Este carrito no le pertenece")
                         .success(false)
                         .status(HttpStatus.BAD_REQUEST.name())
                         .content(null)
                         .build();
             }

             Cart cart = cartRepository.findByUser( userSession ).orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado"));

             return ResponseWrapperDTO.<CartDTO>builder()
                     .message("Carrito cargado correctamente")
                     .success(true)
                     .status(HttpStatus.OK.name())
                     .content(CartDTO.fromEntity( cart ))
                     .build();
         }catch (ResourceNotFoundException e){
             e.printStackTrace();
             return ResponseWrapperDTO.<CartDTO>builder()
                     .message(e.getMessage())
                     .success(false)
                     .status(HttpStatus.NOT_FOUND.name())
                     .content(null)
                     .build();
         }
    }

    public ResponseWrapperDTO<CartDTO> addItemToCart(AddItemDTO addItemDTO){
        try {
            String message = "";
            Cart cart = cartRepository.findById(addItemDTO.cart_id()).orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado"));
            Product product = productRepository.findById(addItemDTO.product_id()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            if( product.getStock() < addItemDTO.amount() ){
                return ResponseWrapperDTO.<CartDTO>builder()
                        .message("La cantidad del producto a agregar excede las existencias")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content( null )
                        .build();
            }

            List<CartItem> cartItemsInCart = cartItemRepository.findByCart( cart );/**/
            CartItem cartItemInCart = cartItemsInCart.stream().filter(i -> i.getProduct().getId().equals( product.getId() )).findFirst().orElse(null);
            if( cartItemInCart == null ){
                message = "Producto agregado al carrito";
                CartItem newCartItem = CartItem.builder()
                        .cart( cart )
                        .amount( addItemDTO.amount() )
                        .product( product )
                        .total( product.getPrice().multiply(BigDecimal.valueOf(addItemDTO.amount())).setScale(2, RoundingMode.HALF_UP))
                        .build();

                cartItemRepository.save( newCartItem );
            }else {
                if( (product.getStock() == cartItemInCart.getAmount()) || (product.getStock() < (cartItemInCart.getAmount() + addItemDTO.amount())) ){
                    return ResponseWrapperDTO.<CartDTO>builder()
                            .message("La cantidad del producto a agregar excede las existencias")
                            .success(false)
                            .status(HttpStatus.BAD_REQUEST.name())
                            .content( null )
                            .build();
                }
                message = "Cantidad aumentada";
                int newAmount = cartItemInCart.getAmount() + addItemDTO.amount();
                cartItemInCart.setAmount(newAmount);
                cartItemInCart.setTotal(cartItemInCart.getProduct().getPrice().multiply(BigDecimal.valueOf(newAmount)).setScale(2, RoundingMode.HALF_UP));
                cartItemRepository.save(cartItemInCart);
            }

            Cart cartToUpdate = cartRepository.findById(cart.getId() ).orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado"));
            cartToUpdate.calculateTotals();
            Cart cartUpdated = cartRepository.save(cartToUpdate);

            return ResponseWrapperDTO.<CartDTO>builder()
                    .message(message)
                    .success(true)
                    .status(HttpStatus.OK.name())
                    .content( CartDTO.fromEntity( cartUpdated ) )
                    .build();
        }catch (ResourceNotFoundException e) {
            e.printStackTrace();
            return ResponseWrapperDTO.<CartDTO>builder()
                    .message("Ocurrio un error al agregar el producto")
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content(null)
                    .build();
        }
    }
    public ResponseWrapperDTO<CartDTO> removeItemToCart(RemoveItemDTO removeItemDTO){
        try {
            Cart cart = cartRepository.findById(removeItemDTO.cart_id()).orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado"));
            CartItem cartItem = cartItemRepository.findById(removeItemDTO.item_id()).orElseThrow(() -> new ResourceNotFoundException("Item no encontrado"));
            String message = "";

            if(removeItemDTO.removeAll()){
                cartItemRepository.deleteById(removeItemDTO.item_id());
                message = "Item removido del carrito";
            }else {
                int newAmount = cartItem.getAmount() - removeItemDTO.amount();
                if( newAmount <= 0){
                    cartItemRepository.deleteById(removeItemDTO.item_id());
                    message = "Item removido del carrito";
                }else {
                    cartItem.setAmount(newAmount);
                    cartItem.setTotal(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(newAmount)).setScale(2, RoundingMode.HALF_UP));
                    cartItemRepository.save( cartItem  );
                    message = "Cantidad reducida";
                }

            }

            Cart cartToUpdate = cartRepository.findById(cart.getId() ).orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado"));
            cartToUpdate.calculateTotals();
            Cart cartUpdated = cartRepository.save( cartToUpdate );

            return ResponseWrapperDTO.<CartDTO>builder()
                    .message(message)
                    .success(true)
                    .status(HttpStatus.OK.name())
                    .content( CartDTO.fromEntity( cartUpdated ) )
                    .build();
        }catch (ResourceNotFoundException e) {
            e.printStackTrace();
            return ResponseWrapperDTO.<CartDTO>builder()
                    .message("Ocurrio un error al remover el producto")
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<CartDTO> updateShippingCost(UpdateShippingCostDTO updateShippingCostDTO){
        try {
            Cart cart = cartRepository.findById(updateShippingCostDTO.cartId()).orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado"));
            cart.setShippingCost( updateShippingCostDTO.shippingCost().setScale(2, RoundingMode.HALF_UP) );
            cart.setDistance(updateShippingCostDTO.distance());
            cart.calculateTotals();
            System.out.println(cart.getTax());

            Cart cartUpdated = cartRepository.save( cart );
            return ResponseWrapperDTO.<CartDTO>builder()
                    .message("Costo de Envio actualizado")
                    .success(true)
                    .status(HttpStatus.OK.name())
                    .content( CartDTO.fromEntity( cartUpdated ) )
                    .build();
        }catch (ResourceNotFoundException e) {
            e.printStackTrace();
            return ResponseWrapperDTO.<CartDTO>builder()
                    .message("Ocurrio un error al actualizar el precio de entrega")
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<CartDTO> clearCart(String cartId){
        try {
            Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado"));
            cart.setTotal(cart.getShippingCost());
            cart.setTax(BigDecimal.ZERO);
            cart.setDiscount(BigDecimal.ZERO);
            cart.setSubtotal(BigDecimal.ZERO);

            cartRepository.save( cart );

            cart.getCartItems().forEach(cartItemRepository::delete);

            Cart cartUpdated = cartRepository.findById( cart.getId() ).get();
            cartUpdated.setCartItems(new ArrayList<>());
            return ResponseWrapperDTO.<CartDTO>builder()
                    .message("Carrito limpiado")
                    .success(true)
                    .status(HttpStatus.OK.name())
                    .content( CartDTO.fromEntity(cartUpdated) )
                    .build();
        }catch (ResourceNotFoundException e) {
            e.printStackTrace();
            return ResponseWrapperDTO.<CartDTO>builder()
                    .message("Ocurrio un error al limpiar el carrito")
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<CartDTO> clearCartByUser(String userId){
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado"));
            cart.setTotal(cart.getShippingCost().setScale(2, RoundingMode.HALF_UP));
            cart.setTax(BigDecimal.ZERO);
            cart.setDiscount(BigDecimal.ZERO);
            cart.setSubtotal(BigDecimal.ZERO);

            cartRepository.save( cart );

            cart.getCartItems().forEach(cartItemRepository::delete);

            Cart cartUpdated = cartRepository.findById( cart.getId() ).get();
            cartUpdated.setCartItems(new ArrayList<>());
            return ResponseWrapperDTO.<CartDTO>builder()
                    .message("Carrito limpiado")
                    .success(true)
                    .status(HttpStatus.OK.name())
                    .content( CartDTO.fromEntity(cartUpdated) )
                    .build();
        }catch (ResourceNotFoundException e) {
            e.printStackTrace();
            return ResponseWrapperDTO.<CartDTO>builder()
                    .message("Ocurrio un error al limpiar el carrito")
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content(null)
                    .build();
        }
    }

}
