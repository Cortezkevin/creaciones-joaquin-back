package com.utp.creacionesjoaquin.controller;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.order.*;
import com.utp.creacionesjoaquin.service.OrderPreparationService;
import com.utp.creacionesjoaquin.service.OrderService;
import com.utp.creacionesjoaquin.service.OrderShippingService;
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
    private final OrderPreparationService orderPreparationService;
    private final OrderShippingService orderShippingService;

    @PreAuthorize("hasAnyAuthority('ROLE_WAREHOUSE','ROLE_TRANSPORT','ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<ResponseWrapperDTO<List<OrderDTO>>> findAll(){
        return ResponseEntity.ok(orderService.findAll());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_WAREHOUSE','ROLE_ADMIN')")
    @GetMapping("/preparation")
    public ResponseEntity<ResponseWrapperDTO<List<OrderPreparationDTO>>> findAllPreparationOrders(){
        return ResponseEntity.ok(orderPreparationService.getAllOrderPreparation());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_TRANSPORT','ROLE_ADMIN')")
    @GetMapping("/shipping")
    public ResponseEntity<ResponseWrapperDTO<List<OrderShippingDTO>>> findAllShippingOrders(){
        return ResponseEntity.ok(orderShippingService.getAllOrderShipping());
    }

    @GetMapping("/preparation/{orderPreparationId}")
    public ResponseEntity<ResponseWrapperDTO<DetailedPreparationOrder>> findOrderPreparationById(
            @PathVariable String orderPreparationId
    ){
        return ResponseEntity.ok(orderPreparationService.findById( orderPreparationId ));
    }

    @GetMapping("/shipping/{orderShippingId}")
    public ResponseEntity<ResponseWrapperDTO<DetailedShippingOrder>> findOrderShippingById(
            @PathVariable String orderShippingId
    ){
        return ResponseEntity.ok(orderShippingService.findById( orderShippingId ));
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

    @PreAuthorize("hasAnyAuthority('ROLE_TRANSPORT','ROLE_ADMIN')")
    @PostMapping("/shipping/start")
    public ResponseEntity<ResponseWrapperDTO<OrderShippingDTO>> startShippingOrder(
            @RequestBody StartOrderShippingDTO startOrderShippingDTO
    ){
        return ResponseEntity.ok( orderShippingService.startShippingOrder( startOrderShippingDTO ) );
    }


    @PreAuthorize("hasAnyAuthority('ROLE_TRANSPORT','ROLE_ADMIN')")
    @PostMapping("/shipping/prepare")
    public ResponseEntity<ResponseWrapperDTO<OrderShippingDTO>> checkPrepareShippingOrder(
            @RequestBody PreparedOrderShippingDTO preparedOrderShippingDTO
    ){
        return ResponseEntity.ok( orderShippingService.checkOrderShippingPrepared( preparedOrderShippingDTO ) );
    }


    @PreAuthorize("hasAnyAuthority('ROLE_TRANSPORT','ROLE_ADMIN')")
    @PostMapping("/shipping/transit")
    public ResponseEntity<ResponseWrapperDTO<OrderShippingDTO>> checkTransitShippingOrder(
            @RequestBody TransitOrderShippingDTO transitOrderShippingDTO
    ){
        return ResponseEntity.ok( orderShippingService.checkOrderShippingTransit( transitOrderShippingDTO ) );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_TRANSPORT','ROLE_ADMIN')")
    @PostMapping("/shipping/complete")
    public ResponseEntity<ResponseWrapperDTO<OrderShippingDTO>> completeShippingOrder(
            @RequestBody CompleteOrderShippingDTO completeOrderShippingDTO
    ){
        return ResponseEntity.ok( orderShippingService.checkOrderShippingComplete( completeOrderShippingDTO ) );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_WAREHOUSE','ROLE_ADMIN')")
    @PostMapping("/preparation/start")
    public ResponseEntity<ResponseWrapperDTO<OrderPreparationDTO>> startPreparationOrder(
            @RequestBody StartOrderPreparationDTO startOrderPreparationDTO
    ){
        return ResponseEntity.ok( orderPreparationService.startPreparationOrder( startOrderPreparationDTO ) );
    }


    @PreAuthorize("hasAnyAuthority('ROLE_WAREHOUSE','ROLE_ADMIN')")
    @PostMapping("/preparation/packaging")
    public ResponseEntity<ResponseWrapperDTO<OrderPreparationDTO>> checkPackagingPreparationOrder(
            @RequestBody PackagingOrderPreparationDTO packagingOrderPreparationDTO
    ){
        return ResponseEntity.ok( orderPreparationService.checkOrderPreparationPackaging( packagingOrderPreparationDTO ) );
    }


    @PreAuthorize("hasAnyAuthority('ROLE_WAREHOUSE','ROLE_ADMIN')")
    @PostMapping("/preparation/complete")
    public ResponseEntity<ResponseWrapperDTO<OrderPreparationDTO>> completePreparationOrder(
            @RequestBody CompletedOrderPreparationDTO completedOrderPreparationDTO
    ){
        return ResponseEntity.ok( orderPreparationService.checkOrderPreparationCompleted( completedOrderPreparationDTO ) );
    }

}
