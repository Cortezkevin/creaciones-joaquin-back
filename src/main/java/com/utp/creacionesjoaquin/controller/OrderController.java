package com.utp.creacionesjoaquin.controller;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.order.DetailedOrderDTO;
import com.utp.creacionesjoaquin.dto.order.InvoiceDTO;
import com.utp.creacionesjoaquin.dto.order.OrderDTO;
import com.utp.creacionesjoaquin.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<ResponseWrapperDTO<List<OrderDTO>>> findAll(){
        return ResponseEntity.ok(orderService.findAll());
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseWrapperDTO<DetailedOrderDTO>> findById(
            @PathVariable String orderId
    ){
        return ResponseEntity.ok(orderService.findById( orderId ));
    }

    @GetMapping("/findBy/{userId}")
    public ResponseEntity<ResponseWrapperDTO<List<OrderDTO>>> findByUser(
            @PathVariable String userId
    ){
        return ResponseEntity.ok(orderService.findByUser(userId));
    }

    @GetMapping("/invoice")
    public ResponseEntity<Resource> exportInvoice(
            @RequestParam("order") String orderId
    ){
        InvoiceDTO invoiceDTO = orderService.exportInvoice( orderId);
        return ResponseEntity.ok()
                .contentLength(invoiceDTO.invoiceLength().longValue())
                .contentType(MediaType.APPLICATION_PDF)
                .headers(invoiceDTO.headers())
                .body(new ByteArrayResource(invoiceDTO.resource()));
    }

}
