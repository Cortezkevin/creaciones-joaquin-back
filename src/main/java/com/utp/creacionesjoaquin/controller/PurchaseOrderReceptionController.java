package com.utp.creacionesjoaquin.controller;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.purchaseOrder.AcceptAndRejectPurchaseOrderDTO;
import com.utp.creacionesjoaquin.dto.purchaseOrder.DetailedPurchaseOrderReceptionDTO;
import com.utp.creacionesjoaquin.dto.purchaseOrder.PurchaseOrderReceptionDTO;
import com.utp.creacionesjoaquin.service.PurchaseOrderReceptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/purchase_order_reception")
@RequiredArgsConstructor
public class PurchaseOrderReceptionController {

    private final PurchaseOrderReceptionService purchaseOrderReceptionService;

    @GetMapping
    public ResponseEntity<ResponseWrapperDTO<List<PurchaseOrderReceptionDTO>>> getAll(){
        return ResponseEntity.ok( purchaseOrderReceptionService.getAll() );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapperDTO<DetailedPurchaseOrderReceptionDTO>> getById(
            @PathVariable String id
    ){
        return ResponseEntity.ok( purchaseOrderReceptionService.getById( id ) );
    }

    @PostMapping("/{id}")
    public ResponseEntity<ResponseWrapperDTO<DetailedPurchaseOrderReceptionDTO>> startOrderReception(
            @PathVariable String id,
            @RequestParam(name = "purchaseOrderId") String purchaseOrderId,
            @RequestParam(name = "grocerId") String grocerId
    ){
        return ResponseEntity.ok( purchaseOrderReceptionService.startOrderReception( purchaseOrderId, id, grocerId ) );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapperDTO<DetailedPurchaseOrderReceptionDTO>> checkReviewOrderReception(
            @PathVariable String id
    ){
        return ResponseEntity.ok( purchaseOrderReceptionService.checkReviewOrderReception( id ) );
    }

    @PutMapping("/acceptOrReject/{id}")
    public ResponseEntity<ResponseWrapperDTO<DetailedPurchaseOrderReceptionDTO>> acceptOrRejectOrderMaterials(
            @PathVariable String id,
            @RequestBody AcceptAndRejectPurchaseOrderDTO acceptAndRejectPurchaseOrderDTO
            ){
        return ResponseEntity.ok( purchaseOrderReceptionService.acceptOrRejectOrderMaterials( id, acceptAndRejectPurchaseOrderDTO ) );
    }
}
