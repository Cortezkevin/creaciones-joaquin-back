package com.utp.creacionesjoaquin.controller;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.cart.AddItemDTO;
import com.utp.creacionesjoaquin.dto.cart.CartDTO;
import com.utp.creacionesjoaquin.dto.cart.RemoveItemDTO;
import com.utp.creacionesjoaquin.dto.cart.UpdateShippingCostDTO;
import com.utp.creacionesjoaquin.service.CartService;
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

    @PutMapping("/shipping")
    public ResponseEntity<ResponseWrapperDTO<CartDTO>> updateShippingCost(
            @RequestBody UpdateShippingCostDTO updateShippingCostDTO
    ){
        return ResponseEntity.ok(cartService.updateShippingCost( updateShippingCostDTO ));
    }
}
