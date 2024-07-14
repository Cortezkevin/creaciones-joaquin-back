package com.utp.creacionesjoaquin.service;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.order.*;
import com.utp.creacionesjoaquin.enums.*;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.model.*;
import com.utp.creacionesjoaquin.repository.*;
import com.utp.creacionesjoaquin.security.model.User;
import com.utp.creacionesjoaquin.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderPreparationService {

    private final OrderShippingRepository orderShippingRepository;
    private final OrderPreparationRepository orderPreparationRepository;
    private final GrocerRepository grocerRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InventoryMovementsRepository inventoryMovementsRepository;
    private final WarehouseRepository warehouseRepository;
    private final ExitGuideRepository exitGuideRepository;

    public ResponseWrapperDTO<List<OrderPreparationDTO>> getAllOrderPreparation(){
        Sort sort = Sort.by( Sort.Direction.DESC, "createdDate" );
        List<OrderPreparationDTO> orderPreparationDTOList = orderPreparationRepository.findAll(sort).stream().map(OrderPreparationDTO::parseToDTO).toList();
        return ResponseWrapperDTO.<List<OrderPreparationDTO>>builder()
                .message("Solicitud satisfactoria")
                .status(HttpStatus.OK.name())
                .success(true)
                .content( orderPreparationDTOList )
                .build();
    }

    public ResponseWrapperDTO<OrderPreparationDTO> startPreparationOrder(StartOrderPreparationDTO startOrderPreparationDTO){
        try {
            Order order = orderRepository.findById(startOrderPreparationDTO.orderId()).orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

            if( order.getStatus() == OrderStatus.ANULADO){
                return ResponseWrapperDTO.<OrderPreparationDTO>builder()
                        .message("El pedido que intenta preparar fue anulado")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content(null)
                        .build();
            }

            User user = userRepository.findById(startOrderPreparationDTO.userId()).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            Grocer grocer = grocerRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Repartidor no encontrado"));

            if( grocer.getStatus() == GrocerStatus.DISPONIBLE ){
                OrderPreparation orderPreparation = orderPreparationRepository.findByOrder( order ).orElseThrow(() -> new ResourceNotFoundException("Preparacion de pedido no encontrado"));

                if(orderPreparation.getStatus() == PreparationStatus.PENDIENTE ){
                    orderPreparation.setStatus(PreparationStatus.EN_PREPARACION);
                    orderPreparation.setStartDate(new Timestamp(new Date().getTime()));

                    grocer.setStatus( GrocerStatus.PROCESANDO_PEDIDO );

                    Grocer grocerUpdated = grocerRepository.save( grocer );

                    orderPreparation.setGrocer( grocerUpdated );

                    order.setStatus( OrderStatus.EN_PROCESO );
                    orderRepository.save(order);

                    OrderPreparation orderPreparationUpdated = orderPreparationRepository.save( orderPreparation );
                    return ResponseWrapperDTO.<OrderPreparationDTO>builder()
                            .message("Se inicio el proceso de preparacion del pedido: #"+order.getId())
                            .success(true)
                            .status(HttpStatus.OK.name())
                            .content(OrderPreparationDTO.parseToDTO( orderPreparationUpdated ))
                            .build();
                }else {
                    return ResponseWrapperDTO.<OrderPreparationDTO>builder()
                            .message("Este pedido ya fue iniciado por otro trabajador")
                            .success(false)
                            .status(HttpStatus.BAD_REQUEST.name())
                            .content(null)
                            .build();
                }
            }else {
                return ResponseWrapperDTO.<OrderPreparationDTO>builder()
                        .message("Para iniciar con otro pedido debe culminar el que ya inicio.")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content(null)
                        .build();
            }
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<OrderPreparationDTO>builder()
                    .message("Ocurrio un error al iniciar: " + e.getMessage())
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<OrderPreparationDTO> checkOrderPreparationPackaging(PackagingOrderPreparationDTO packagingOrderPreparationDTO){
        try {
            OrderPreparation orderPreparation = orderPreparationRepository.findById( packagingOrderPreparationDTO.preparationOrderId() ).orElseThrow(() -> new ResourceNotFoundException("Preparacion de pedido no encontrado"));
            Grocer grocer = grocerRepository.findById( orderPreparation.getGrocer().getId() ).orElseThrow(() -> new ResourceNotFoundException("Almacenero no encontrado"));
            Order order = orderRepository.findById( orderPreparation.getOrder().getId() ).orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

            if( order.getStatus() == OrderStatus.ANULADO){
                return ResponseWrapperDTO.<OrderPreparationDTO>builder()
                        .message("El pedido fue anulado")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content(null)
                        .build();
            }

            if(orderPreparation.getStatus() != PreparationStatus.EN_PREPARACION ){
                return ResponseWrapperDTO.<OrderPreparationDTO>builder()
                        .message("La preparacion del pedido debe pasar por el estado EN_PREPARACION para continuar")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content(null)
                        .build();
            }

            orderPreparation.setStatus( PreparationStatus.EN_EMPAQUETADO );
            orderPreparation.setPreparedDate(new Timestamp(new Date().getTime()));

            grocer.setStatus( GrocerStatus.EMPAQUETANDO );
            Grocer grocerUpdated = grocerRepository.save( grocer );

            orderPreparation.setGrocer( grocerUpdated );
            OrderPreparation orderPreparationUpdated = orderPreparationRepository.save( orderPreparation );
            return ResponseWrapperDTO.<OrderPreparationDTO>builder()
                    .message("Pedido en empaquetado")
                    .success(true)
                    .status(HttpStatus.OK.name())
                    .content(OrderPreparationDTO.parseToDTO( orderPreparationUpdated ))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<OrderPreparationDTO>builder()
                    .message("Ocurrio un error al cambiar de estado: "+ e.getMessage() )
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<OrderPreparationDTO> checkOrderPreparationCompleted(CompletedOrderPreparationDTO completedOrderPreparationDTO){
        try {
            OrderPreparation orderPreparation = orderPreparationRepository.findById( completedOrderPreparationDTO.orderPreparationId() ).orElseThrow(() -> new ResourceNotFoundException("Entrega de pedido no encontrado"));
            Grocer grocer = grocerRepository.findById( orderPreparation.getGrocer().getId() ).orElseThrow(() -> new ResourceNotFoundException("Almacenero no encontrado"));
            Order order = orderRepository.findById( orderPreparation.getOrder().getId() ).orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));
            Warehouse warehouse = warehouseRepository.findById(completedOrderPreparationDTO.warehouse() ).orElseThrow(() -> new ResourceNotFoundException("Almacen no encontrado"));

            if( order.getStatus() == OrderStatus.ANULADO){
                return ResponseWrapperDTO.<OrderPreparationDTO>builder()
                        .message("El pedido fue anulado")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content(null)
                        .build();
            }

            if(orderPreparation.getStatus() != PreparationStatus.EN_EMPAQUETADO ){
                return ResponseWrapperDTO.<OrderPreparationDTO>builder()
                        .message("El pedido debe pasar por el estado EN_EMPAQUETADO para continuar")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content(null)
                        .build();
            }

            ExitGuide newExitGuide = ExitGuide.builder()
                    .date(new Timestamp(System.currentTimeMillis()))
                    .observations(completedOrderPreparationDTO.observations())
                    .order(order)
                    .grocer( grocer )
                    .warehouse( warehouse )
                    .build();

            ExitGuide exitGuideCreated = exitGuideRepository.save( newExitGuide );

            List<InventoryMovements> newInventoryMovementsList = new ArrayList<>();
            order.getOrderDetails().forEach(o -> {
                Product product = productRepository.findById(  o.getProduct().getId() ).get();
                int newStock = product.getStock() - o.getAmount();

                InventoryMovements inventoryMovements = InventoryMovements.builder()
                        .date( new Timestamp(System.currentTimeMillis()))
                        .product(product)
                        .type(InventoryMovementType.SALIDA)
                        .reason("Venta")
                        .initialStock( product.getStock() )
                        .amount( o.getAmount() )
                        .newStock( newStock )
                        .exitGuide( exitGuideCreated )
                        .warehouse( warehouse )
                        .build();

                product.setStock( newStock );
                productRepository.save( product );

                newInventoryMovementsList.add( inventoryMovements );
            });

            inventoryMovementsRepository.saveAll( newInventoryMovementsList );

            orderPreparation.setStatus( PreparationStatus.LISTO_PARA_RECOGER );
            orderPreparation.setCompletedDate(new Timestamp(new Date().getTime()));
            order.setStatus(OrderStatus.PREPARADO);

            grocer.setStatus(GrocerStatus.DISPONIBLE);

            Grocer grocerUpdated = grocerRepository.save( grocer );
            orderPreparation.setGrocer( grocerUpdated );

            OrderShipping newOrderShipping = OrderShipping.builder()
                    .order( order )
                    .preparedBy( grocerUpdated.getUser().getPersonalInformation().getFullName() )
                    .address( order.getShippingAddress() )
                    .distance( order.getDistance() )
                    .createdDate( new Timestamp(new Date().getTime()))
                    .status(ShippingStatus.PENDIENTE)
                    .build();

            OrderShipping orderShippingCreated = orderShippingRepository.save( newOrderShipping );
            order.setOrderShipping( orderShippingCreated );
            order.setExitGuide( exitGuideCreated );

            orderRepository.save( order );
            OrderPreparation orderPreparationUpdated = orderPreparationRepository.save( orderPreparation );
            return ResponseWrapperDTO.<OrderPreparationDTO>builder()
                    .message("Se completo el proceso de preparacion del pedido")
                    .success(true)
                    .status(HttpStatus.OK.name())
                    .content(OrderPreparationDTO.parseToDTO( orderPreparationUpdated ))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<OrderPreparationDTO>builder()
                    .message("Ocurrio un error al completar la preparacion")
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<DetailedPreparationOrder> findById(String orderPreparationId) {
        try {
            OrderPreparation orderPreparation = orderPreparationRepository.findById( orderPreparationId ).orElseThrow(() -> new ResourceNotFoundException("Preparacion de pedido no encontrado"));
            return ResponseWrapperDTO.<DetailedPreparationOrder>builder()
                    .message("Solicitud satisfactoria")
                    .success(true)
                    .status(HttpStatus.OK.name())
                    .content(DetailedPreparationOrder.parseToDTO(orderPreparation))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<DetailedPreparationOrder>builder()
                    .message("Ocurrio un error: "+ e.getMessage())
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content(null)
                    .build();
        }
    }
}
