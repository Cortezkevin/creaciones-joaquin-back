package com.utp.creacionesjoaquin.service;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.purchaseOrder.AcceptAndRejectPurchaseOrderDTO;
import com.utp.creacionesjoaquin.dto.purchaseOrder.DetailedPurchaseOrderReceptionDTO;
import com.utp.creacionesjoaquin.dto.purchaseOrder.PurchaseOrderReceptionDTO;
import com.utp.creacionesjoaquin.enums.*;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.model.*;
import com.utp.creacionesjoaquin.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderReceptionService {

    private final GrocerRepository grocerRepository;
    private final PurchaseOrderReceptionRepository purchaseOrderReceptionRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;
    private final EntryGuideRepository entryGuideRepository;
    private final RejectionGuideRepository rejectionGuideRepository;
    private final InventoryMovementsRepository inventoryMovementsRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    public ResponseWrapperDTO<DetailedPurchaseOrderReceptionDTO> getById(String id){
        try {
            PurchaseOrderReception purchaseOrderReception = purchaseOrderReceptionRepository.findById( id ).orElseThrow(() -> new ResourceNotFoundException("Recepcion de Orden de compra no encontrada"));
            return ResponseWrapperDTO.<DetailedPurchaseOrderReceptionDTO>builder()
                    .success(true)
                    .message("Proceso de Revision Iniciada")
                    .status(HttpStatus.OK.name())
                    .content(DetailedPurchaseOrderReceptionDTO.parseToDTO( purchaseOrderReception ))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<DetailedPurchaseOrderReceptionDTO>builder()
                    .message(e.getMessage())
                    .success(false)
                    .status(HttpStatus.NOT_FOUND.name())
                    .content( null )
                    .build();
        }
    }

    public ResponseWrapperDTO<List<PurchaseOrderReceptionDTO>> getAll(){
        List<PurchaseOrderReceptionDTO> purchaseOrderReceptionDTOList = purchaseOrderReceptionRepository.findAll()
                .stream().map(PurchaseOrderReceptionDTO::parseToDTO).toList();
        return ResponseWrapperDTO.<List<PurchaseOrderReceptionDTO>>builder()
                .message("Solicitud satisfactoria")
                .success(true)
                .status(HttpStatus.OK.name())
                .content( purchaseOrderReceptionDTOList )
                .build();
    }

    public ResponseWrapperDTO<DetailedPurchaseOrderReceptionDTO> startOrderReception(String purchaseOrderId, String receptionId, String grocerId){
        try {
            PurchaseOrder purchaseOrder = purchaseOrderRepository.findById( purchaseOrderId ).orElseThrow(() -> new ResourceNotFoundException("Orden de compra no encontrada"));

            if( purchaseOrder.getStatus().equals(PurchaseOrderStatus.CANCELADA)){
                return ResponseWrapperDTO.<DetailedPurchaseOrderReceptionDTO>builder()
                        .message("La orden de compra fue cancelada/anulada")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content( null )
                        .build();
            }

            Grocer grocer = grocerRepository.findById( grocerId ).orElseThrow(() -> new ResourceNotFoundException("Almacenero no encontrado"));

            if( !grocer.getStatus().equals(GrocerStatus.DISPONIBLE)) {
                return ResponseWrapperDTO.<DetailedPurchaseOrderReceptionDTO>builder()
                        .message("Para iniciar la recepcion de la compra debe estar previamente disponible.")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content( null )
                        .build();
            }

            //grocer.setStatus( GrocerStatus.RECEPCIONANDO_ORDEN );
            Grocer grocerUpdated = grocerRepository.save( grocer );

            PurchaseOrderReception purchaseOrderReception = purchaseOrderReceptionRepository.findById( receptionId ).orElseThrow(() -> new ResourceNotFoundException("Recepcion de Orden de compra no encontrada"));
            purchaseOrderReception.setStartDate( new Timestamp(System.currentTimeMillis()));
            purchaseOrderReception.setGrocer( grocerUpdated );
            purchaseOrderReception.setStatus( PurchaseOrderReceptionStatus.RECIBIDO );

            PurchaseOrderReception purchaseOrderReceptionUpdated = purchaseOrderReceptionRepository.save( purchaseOrderReception );
            purchaseOrder.setStatus( PurchaseOrderStatus.RECIBIDA );
            purchaseOrder.setPurchaseOrderReception( purchaseOrderReceptionUpdated );

            purchaseOrderRepository.save( purchaseOrder );

            return ResponseWrapperDTO.<DetailedPurchaseOrderReceptionDTO>builder()
                    .success(true)
                    .message("Inicio proceso de recepcion de la orden #"+purchaseOrder.getId() )
                    .status(HttpStatus.OK.name())
                    .content(DetailedPurchaseOrderReceptionDTO.parseToDTO( purchaseOrderReceptionUpdated ))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<DetailedPurchaseOrderReceptionDTO>builder()
                    .message(e.getMessage())
                    .success(false)
                    .status(HttpStatus.NOT_FOUND.name())
                    .content( null )
                    .build();
        }
    }

    public ResponseWrapperDTO<DetailedPurchaseOrderReceptionDTO> checkReviewOrderReception(String receptionId){
        try {
            PurchaseOrderReception purchaseOrderReception = purchaseOrderReceptionRepository.findById( receptionId ).orElseThrow(() -> new ResourceNotFoundException("Recepcion de Orden de compra no encontrada"));
            PurchaseOrder purchaseOrder = purchaseOrderRepository.findById( purchaseOrderReception.getPurchaseOrder().getId() ).orElseThrow(() -> new ResourceNotFoundException("Orden de compra no encontrada"));

            purchaseOrder.setStatus( PurchaseOrderStatus.EN_REVISION );

            purchaseOrderReception.setStatus(PurchaseOrderReceptionStatus.EN_REVISION);
            purchaseOrderReception.setReviewDate( new Timestamp(System.currentTimeMillis()));

            PurchaseOrderReception purchaseOrderReceptionUpdated = purchaseOrderReceptionRepository.save( purchaseOrderReception );
            purchaseOrder.setPurchaseOrderReception( purchaseOrderReceptionUpdated );

            purchaseOrderRepository.save( purchaseOrder );

            return ResponseWrapperDTO.<DetailedPurchaseOrderReceptionDTO>builder()
                    .success(true)
                    .message("Se inicio la revision de la orden")
                    .status(HttpStatus.OK.name())
                    .content(DetailedPurchaseOrderReceptionDTO.parseToDTO( purchaseOrderReceptionUpdated ))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<DetailedPurchaseOrderReceptionDTO>builder()
                    .message(e.getMessage())
                    .success(false)
                    .status(HttpStatus.NOT_FOUND.name())
                    .content( null )
                    .build();
        }
    }

    public ResponseWrapperDTO<DetailedPurchaseOrderReceptionDTO> acceptOrRejectOrderMaterials(String receptionId, AcceptAndRejectPurchaseOrderDTO acceptAndRejectPurchaseOrderDTO){
        try {
            PurchaseOrderReception purchaseOrderReception = purchaseOrderReceptionRepository.findById( receptionId ).orElseThrow(() -> new ResourceNotFoundException("Recepcion de Orden de compra no encontrada"));
            PurchaseOrder purchaseOrder = purchaseOrderRepository.findById( purchaseOrderReception.getPurchaseOrder().getId() ).orElseThrow(() -> new ResourceNotFoundException("Orden de compra no encontrada"));

            purchaseOrder.setStatus( PurchaseOrderStatus.COMPLETADA );

            purchaseOrderReception.setStatus(PurchaseOrderReceptionStatus.COMPLETADO);
            purchaseOrderReception.setCompletedDate( new Timestamp(System.currentTimeMillis()));

            Grocer grocer = purchaseOrderReception.getGrocer();

            grocer.setStatus( GrocerStatus.DISPONIBLE );

            Grocer grocerUpdated = grocerRepository.save( grocer );

            purchaseOrderReception.setGrocer( grocerUpdated );

            PurchaseOrderReception purchaseOrderReceptionUpdated = purchaseOrderReceptionRepository.save( purchaseOrderReception );

            purchaseOrder.setPurchaseOrderReception( purchaseOrderReceptionUpdated );

            List<PurchaseOrderDetail> orderDetailList = purchaseOrder.getPurchaseOrderDetails().stream().map(d -> {
                if(acceptAndRejectPurchaseOrderDTO.acceptedOrderDetailIds().stream().anyMatch(i -> i.equals(d.getId()))){
                    d.setStatus( PurchaseOrderDetailStatus.ACEPTADO );
                }else {
                    d.setStatus( PurchaseOrderDetailStatus.RECHAZADO );
                }
                return d;
            }).toList();

            List<PurchaseOrderDetail> purchaseOrderDetailListUpdated = purchaseOrderDetailRepository.saveAll( orderDetailList );

            List<PurchaseOrderDetail> rejectedOrderDetails = purchaseOrderDetailListUpdated.stream().filter(o -> o.getStatus().equals(PurchaseOrderDetailStatus.RECHAZADO)).toList();
            if( rejectedOrderDetails.size() > 0 ){
                RejectionGuide newRejectionGuide = RejectionGuide.builder()
                        .reason(acceptAndRejectPurchaseOrderDTO.rejectReason())
                        .suggestions(acceptAndRejectPurchaseOrderDTO.suggestions())
                        .purchaseOrder(purchaseOrder)
                        .productConditions(acceptAndRejectPurchaseOrderDTO.rejectConditions())
                        .supplier(purchaseOrder.getSupplier())
                        .date(new Timestamp(System.currentTimeMillis()))
                        .build();

                RejectionGuide rejectionGuideCreated = rejectionGuideRepository.save( newRejectionGuide );
                purchaseOrder.setRejectionGuide( rejectionGuideCreated );
            }
            if (rejectedOrderDetails.size() != purchaseOrderDetailListUpdated.size() ) {
                Warehouse warehouse = warehouseRepository.findById( acceptAndRejectPurchaseOrderDTO.warehouseLocation() ).orElseThrow(() -> new ResourceNotFoundException("Almacen no encontrado"));

                EntryGuide newEntryGuide = EntryGuide.builder()
                        .date(new Timestamp(System.currentTimeMillis()))
                        .purchaseOrder(purchaseOrder)
                        .supplier(purchaseOrder.getSupplier())
                        .productConditions(acceptAndRejectPurchaseOrderDTO.acceptConditions())
                        .warehouseLocation(warehouse.getLocation())
                        .build();

                EntryGuide entryGuideCreated = entryGuideRepository.save( newEntryGuide );

                List<InventoryMovements> newInventoryMovementsList = new ArrayList<>();
                purchaseOrderDetailListUpdated.forEach( pod -> {
                    InventoryMovements newInventoryMovements = InventoryMovements.builder()
                            .amount(pod.getAmount())
                            .date(new Timestamp(System.currentTimeMillis()))
                            .type(InventoryMovementType.ENTRADA)
                            .warehouse(warehouse)
                            .build();
                    RawMaterial rawMaterial = pod.getRawMaterial();
                    if( rawMaterial != null ){
                        newInventoryMovements.setRawMaterial( rawMaterial );
                        rawMaterial.setStock( rawMaterial.getStock() + pod.getAmount() );
                        rawMaterialRepository.save( rawMaterial );
                    }else{
                        Product product = pod.getProduct();
                        newInventoryMovements.setProduct( product );
                        product.setStock( product.getStock() + pod.getAmount() );
                        productRepository.save( product );
                    }
                    newInventoryMovementsList.add( newInventoryMovements );
                });

                inventoryMovementsRepository.saveAll(newInventoryMovementsList);
                purchaseOrder.setEntryGuide( entryGuideCreated );
            }

            purchaseOrder.setPurchaseOrderDetails(purchaseOrderDetailListUpdated);
            purchaseOrderRepository.save( purchaseOrder );

            return ResponseWrapperDTO.<DetailedPurchaseOrderReceptionDTO>builder()
                    .success(true)
                    .message("Se completo la recepcion de la orden")
                    .status(HttpStatus.OK.name())
                    .content(DetailedPurchaseOrderReceptionDTO.parseToDTO( purchaseOrderReceptionUpdated ))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<DetailedPurchaseOrderReceptionDTO>builder()
                    .message(e.getMessage())
                    .success(false)
                    .status(HttpStatus.NOT_FOUND.name())
                    .content( null )
                    .build();
        }
    }
}
