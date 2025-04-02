package com.utp.creacionesjoaquin.purchase.controller;

import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.purchase.dto.purchaseOrder.CreatePurchaseOrderDTO;
import com.utp.creacionesjoaquin.purchase.dto.purchaseOrder.DetailedPurchaseOrderDTO;
import com.utp.creacionesjoaquin.purchase.dto.purchaseOrder.PurchaseOrderDTO;
import com.utp.creacionesjoaquin.purchase.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/purchase_order")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @GetMapping
    public ResponseEntity<ResponseWrapperDTO<List<PurchaseOrderDTO>>> getAll(){
        return ResponseEntity.ok( purchaseOrderService.getAll() );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapperDTO<DetailedPurchaseOrderDTO>> getById(
            @PathVariable String id
    ){
        return ResponseEntity.ok( purchaseOrderService.getById( id ) );
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<ResponseWrapperDTO<DetailedPurchaseOrderDTO>> cancelPurchaseOrder(
            @PathVariable String id
    ){
        return ResponseEntity.ok( purchaseOrderService.cancelPurchaseOrder( id ) );
    }

    @PostMapping
    public ResponseEntity<ResponseWrapperDTO<PurchaseOrderDTO>> create(
            @RequestBody CreatePurchaseOrderDTO createPurchaseOrderDTO
            ){
        return ResponseEntity.ok( purchaseOrderService.create( createPurchaseOrderDTO ) );
    }
}
