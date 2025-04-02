package com.utp.creacionesjoaquin.purchase.service;

import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.purchase.dto.purchaseOrder.CreatePurchaseOrderDTO;
import com.utp.creacionesjoaquin.purchase.dto.purchaseOrder.DetailedPurchaseOrderDTO;
import com.utp.creacionesjoaquin.purchase.dto.purchaseOrder.PurchaseOrderDTO;
import com.utp.creacionesjoaquin.commons.enums.PurchaseOrderDetailStatus;
import com.utp.creacionesjoaquin.commons.enums.PurchaseOrderReceptionStatus;
import com.utp.creacionesjoaquin.commons.enums.PurchaseOrderStatus;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.administration.model.Product;
import com.utp.creacionesjoaquin.purchase.model.*;
import com.utp.creacionesjoaquin.purchase.repository.*;
import com.utp.creacionesjoaquin.administration.repository.ProductRepository;
import com.utp.creacionesjoaquin.security.enums.RolName;
import com.utp.creacionesjoaquin.security.model.User;
import com.utp.creacionesjoaquin.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final UserRepository userRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderReceptionRepository purchaseOrderReceptionRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;
    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    public ResponseWrapperDTO<List<PurchaseOrderDTO>> getAll(){
        List<PurchaseOrderDTO> purchaseOrderList = purchaseOrderRepository.findAll().stream().map(PurchaseOrderDTO::parseToDTO).toList();
        return ResponseWrapperDTO.<List<PurchaseOrderDTO>>builder()
                .message("Solicitud satisfactoria")
                .success(true)
                .status(HttpStatus.OK.name())
                .content(purchaseOrderList)
                .build();
    }

    public ResponseWrapperDTO<DetailedPurchaseOrderDTO> getById(String purchaseId){
        try {
            PurchaseOrder purchaseOrder = purchaseOrderRepository.findById( purchaseId ).orElseThrow(() -> new ResourceNotFoundException("Orden de compra no encontrada"));
            return ResponseWrapperDTO.<DetailedPurchaseOrderDTO>builder()
                    .success(true)
                    .message("Solicitud satisfactoria")
                    .status(HttpStatus.OK.name())
                    .content( DetailedPurchaseOrderDTO.parseToDTO( purchaseOrder ) )
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<DetailedPurchaseOrderDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content( null )
                    .build();
        }
    }

    public ResponseWrapperDTO<DetailedPurchaseOrderDTO> cancelPurchaseOrder(String purchaseId){
        try {
            PurchaseOrder purchaseOrder = purchaseOrderRepository.findById( purchaseId ).orElseThrow(() -> new ResourceNotFoundException("Orden de compra no encontrada"));
            purchaseOrder.setStatus( PurchaseOrderStatus.CANCELADA );
            PurchaseOrderReception purchaseOrderReception = purchaseOrder.getPurchaseOrderReception();
            purchaseOrderReception.setStatus( PurchaseOrderReceptionStatus.CANCELADO );

            PurchaseOrderReception purchaseOrderReceptionUpdated = purchaseOrderReceptionRepository.save( purchaseOrderReception );
            purchaseOrder.setPurchaseOrderReception( purchaseOrderReceptionUpdated );

            PurchaseOrder purchaseOrderUpdated = purchaseOrderRepository.save( purchaseOrder );
            return ResponseWrapperDTO.<DetailedPurchaseOrderDTO>builder()
                    .success(true)
                    .message("Solicitud satisfactoria")
                    .status(HttpStatus.OK.name())
                    .content( DetailedPurchaseOrderDTO.parseToDTO( purchaseOrderUpdated ) )
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<DetailedPurchaseOrderDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content( null )
                    .build();
        }
    }

    public ResponseWrapperDTO<PurchaseOrderDTO> create(CreatePurchaseOrderDTO createPurchaseOrderDTO){
        try {
            User user = userRepository.findById( createPurchaseOrderDTO.userId() ).orElseThrow(() -> new ResolutionException("Usuario no encontrado"));
            if(user.getRoles().stream().noneMatch(r -> r.getRolName().equals(RolName.ROLE_ADMIN))){
                return ResponseWrapperDTO.<PurchaseOrderDTO>builder()
                        .message("No tiene permisos para iniciar una orden de compra")
                        .success(false)
                        .content(null)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .build();
            }

            Supplier supplier = supplierRepository.findById(createPurchaseOrderDTO.supplierId() ).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));


            PurchaseOrder newPurchaseOrder = PurchaseOrder.builder()
                    .date( new Timestamp(new Date().getTime()) )
                    .supplier( supplier )
                    .status(PurchaseOrderStatus.PENDIENTE)
                    .user( user )
                    .build();

            PurchaseOrder purchaseOrderCreated = purchaseOrderRepository.save( newPurchaseOrder );

            PurchaseOrderReception newPurchaseOrderReception = PurchaseOrderReception.builder()
                    .createdDate( new Timestamp(new Date().getTime()) )
                    .purchaseOrder( purchaseOrderCreated )
                    .status(PurchaseOrderReceptionStatus.PENDIENTE)
                    .build();

            PurchaseOrderReception purchaseOrderReceptionCreated = purchaseOrderReceptionRepository.save( newPurchaseOrderReception );

            purchaseOrderCreated.setPurchaseOrderReception( purchaseOrderReceptionCreated );

            List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();


            createPurchaseOrderDTO.details().forEach( d -> {
                BigDecimal itemTotal = d.unitPrice().multiply(BigDecimal.valueOf(d.amount()));
                PurchaseOrderDetail purchaseOrderDetail = PurchaseOrderDetail.builder()
                        .amount(d.amount())
                        .unitPrice(d.unitPrice())
                        .total(itemTotal)
                        .purchaseOrder( purchaseOrderCreated )
                        .status(PurchaseOrderDetailStatus.NO_RECEPCIONADO)
                        .build();

                Product product = productRepository.findById(d.materialOrProductId()).orElse(null);

                if( product != null){
                    purchaseOrderDetail.setProduct( product );
                }else {
                    RawMaterial rawMaterial = rawMaterialRepository.findById( d.materialOrProductId() ).orElse(null);
                    if(rawMaterial != null){
                        purchaseOrderDetail.setRawMaterial( rawMaterial );
                    }
                }

                purchaseOrderDetails.add( purchaseOrderDetail );
            });

            List<PurchaseOrderDetail> purchaseOrderDetailsCreated = purchaseOrderDetailRepository.saveAll( purchaseOrderDetails );
            purchaseOrderCreated.setPurchaseOrderDetails( purchaseOrderDetailsCreated );

            BigDecimal total = BigDecimal.ZERO;
            for(PurchaseOrderDetail p: purchaseOrderDetailsCreated){
                total = total.add( p.getTotal() );
            }

            purchaseOrderCreated.setTotal(total);
            PurchaseOrder purchaseOrderUpdated = purchaseOrderRepository.save( purchaseOrderCreated );

            return ResponseWrapperDTO.<PurchaseOrderDTO>builder()
                    .message("Orden de compra creado")
                    .success(true)
                    .content(PurchaseOrderDTO.parseToDTO(purchaseOrderUpdated))
                    .status(HttpStatus.OK.name())
                    .build();
        }catch (ResourceNotFoundException e) {
            return ResponseWrapperDTO.<PurchaseOrderDTO>builder()
                    .message(e.getMessage())
                    .success(false)
                    .content(null)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .build();
        }
    }
}
