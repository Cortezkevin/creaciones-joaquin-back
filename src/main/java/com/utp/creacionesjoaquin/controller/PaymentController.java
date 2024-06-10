package com.utp.creacionesjoaquin.controller;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.payment.PaymentIndentResponseDTO;
import com.utp.creacionesjoaquin.service.CartService;
import com.utp.creacionesjoaquin.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    @Value("${front.url}")
    private String FRONT_PATH;

    private final PaymentService paymentService;
    private final CartService cartService;

    @GetMapping("/success")
    public ResponseEntity<?> successPayment(
            @RequestParam("user") String userId
    ){
        ResponseWrapperDTO<String> response = paymentService.successPayment( userId );
        if( response.getSuccess() ){
            cartService.clearCartByUser(userId);
        }else {
            log.error(response.getMessage());
        }

        /* https://creaciones-joaquin-front.vercel.app/ */
        /* http://localhost:3000/ */
        //String externalUrl = "https://creaciones-joaquin-front.vercel.app/cart/checkout/completion";
        String externalUrl = FRONT_PATH + "/cart/checkout/completion";
        return ResponseEntity.status(302).location(ServletUriComponentsBuilder.fromUriString(externalUrl).build().toUri()).build();
    }

    @PostMapping("/createIndent")
    public ResponseEntity<ResponseWrapperDTO<PaymentIndentResponseDTO>> createPaymentIndent(
            @RequestParam("user") String userId
    ){
        return ResponseEntity.ok( paymentService.createIndent( userId ) );
    }

}
