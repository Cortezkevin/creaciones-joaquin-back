package com.utp.creacionesjoaquin.sales.controller;

import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.sales.dto.cart.AddItemDTO;
import com.utp.creacionesjoaquin.sales.dto.cart.CartDTO;
import com.utp.creacionesjoaquin.sales.dto.cart.RemoveItemDTO;
import com.utp.creacionesjoaquin.sales.dto.cart.UpdateShippingCostDTO;
import com.utp.creacionesjoaquin.sales.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/fromUser")
    public ResponseEntity<ResponseWrapperDTO<CartDTO>> addToCart(
            @RequestParam(name = "user") String userId
    ){
        return ResponseEntity.ok(cartService.getCartFromUser( userId ));
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseWrapperDTO<CartDTO>> addToCart(
            @RequestBody AddItemDTO addItemDTO
            ){
        return ResponseEntity.ok(cartService.addItemToCart( addItemDTO ));
    }

    @PostMapping("/remove")
    public ResponseEntity<ResponseWrapperDTO<CartDTO>> removeToCart(
            @RequestBody RemoveItemDTO removeItemDTO
    ){
        return ResponseEntity.ok(cartService.removeItemToCart( removeItemDTO ));
    }

    @PostMapping("/clear")
    public ResponseEntity<ResponseWrapperDTO<CartDTO>> clearCart(
            @RequestParam("cart") String cart
    ){
        return ResponseEntity.ok(cartService.clearCart( cart ));
    }

    @PutMapping("/shipping")
    public ResponseEntity<ResponseWrapperDTO<CartDTO>> updateShippingCost(
            @RequestBody UpdateShippingCostDTO updateShippingCostDTO
    ){
        return ResponseEntity.ok(cartService.updateShippingCost( updateShippingCostDTO ));
    }
}
