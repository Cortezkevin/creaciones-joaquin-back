package com.utp.creacionesjoaquin.service;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.order.*;
import com.utp.creacionesjoaquin.enums.*;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.model.*;
import com.utp.creacionesjoaquin.repository.GrocerRepository;
import com.utp.creacionesjoaquin.repository.OrderPreparationRepository;
import com.utp.creacionesjoaquin.repository.OrderRepository;
import com.utp.creacionesjoaquin.repository.OrderShippingRepository;
import com.utp.creacionesjoaquin.security.model.User;
import com.utp.creacionesjoaquin.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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

    public ResponseWrapperDTO<List<OrderPreparationDTO>> getAllOrderPreparation(){
        List<OrderPreparationDTO> orderPreparationDTOList = orderPreparationRepository.findAll().stream().map(OrderPreparationDTO::parseToDTO).toList();
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
                    .createdDate( new Timestamp(new Date().getTime()))
                    .status(ShippingStatus.PENDIENTE)
                    .build();

            OrderShipping orderShippingCreated = orderShippingRepository.save( newOrderShipping );
            order.setOrderShipping( orderShippingCreated );

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
