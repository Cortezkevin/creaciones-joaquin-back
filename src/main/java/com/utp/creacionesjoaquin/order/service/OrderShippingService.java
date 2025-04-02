package com.utp.creacionesjoaquin.order.service;

import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.commons.enums.CarrierStatus;
import com.utp.creacionesjoaquin.commons.enums.OrderStatus;
import com.utp.creacionesjoaquin.commons.enums.ShippingStatus;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.order.dto.order.*;
import com.utp.creacionesjoaquin.warehouse.model.Carrier;
import com.utp.creacionesjoaquin.order.model.Order;
import com.utp.creacionesjoaquin.order.model.OrderShipping;
import com.utp.creacionesjoaquin.warehouse.repository.CarrierRepository;
import com.utp.creacionesjoaquin.order.repository.OrderRepository;
import com.utp.creacionesjoaquin.order.repository.OrderShippingRepository;
import com.utp.creacionesjoaquin.security.model.User;
import com.utp.creacionesjoaquin.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderShippingService {

    private final OrderShippingRepository orderShippingRepository;
    private final CarrierRepository carrierRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public ResponseWrapperDTO<List<OrderShippingDTO>> getAllOrderShipping(){
        Sort sort = Sort.by( Sort.Direction.DESC, "createdDate" );
        List<OrderShippingDTO> orderShippingDTOList = orderShippingRepository.findAll(sort).stream().map(OrderShippingDTO::parseToDTO).toList();
        return ResponseWrapperDTO.<List<OrderShippingDTO>>builder()
                .message("Solicitud satisfactoria")
                .status(HttpStatus.OK.name())
                .success(true)
                .content( orderShippingDTOList )
                .build();
    }

    public ResponseWrapperDTO<OrderShippingDTO> startShippingOrder(StartOrderShippingDTO startOrderShippingDTO){
        try {
            Order order = orderRepository.findById(startOrderShippingDTO.orderId()).orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

            if( order.getStatus() == OrderStatus.ANULADO){
                return ResponseWrapperDTO.<OrderShippingDTO>builder()
                        .message("El pedido que intenta iniciar fue anulado")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content(null)
                        .build();
            }

            User user = userRepository.findById(startOrderShippingDTO.userId()).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            Carrier carrier = carrierRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Repartidor no encontrado"));

            if( carrier.getStatus() == CarrierStatus.FUERA_DE_SERVICIO){
                return ResponseWrapperDTO.<OrderShippingDTO>builder()
                        .message("No puede iniciar un proceso si esta fuera de servicio")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content(null)
                        .build();
            }

            if( carrier.getStatus() == CarrierStatus.DISPONIBLE ){
                OrderShipping orderShipping = orderShippingRepository.findByOrder( order ).orElseThrow(() -> new ResourceNotFoundException("Entrega de pedido no encontrado"));

                if(orderShipping.getStatus() == ShippingStatus.PENDIENTE ){
                    orderShipping.setCarrier( carrier );
                    orderShipping.setStatus(ShippingStatus.EN_PREPARACION);
                    orderShipping.setStartDate( new Timestamp(new Date().getTime()));

                    carrier.setStatus( CarrierStatus.PROCESANDO_PEDIDO );

                    Carrier carrierUpdated = carrierRepository.save( carrier );

                    orderShipping.setCarrier( carrierUpdated );
                    OrderShipping orderShippingUpdated = orderShippingRepository.save( orderShipping );
                    return ResponseWrapperDTO.<OrderShippingDTO>builder()
                            .message("Se inicio el proceso de entrega del pedido: #"+order.getId())
                            .success(true)
                            .status(HttpStatus.OK.name())
                            .content(OrderShippingDTO.parseToDTO( orderShippingUpdated ))
                            .build();
                }else {
                    return ResponseWrapperDTO.<OrderShippingDTO>builder()
                            .message("Este pedido ya fue iniciado por otro repartidor")
                            .success(false)
                            .status(HttpStatus.BAD_REQUEST.name())
                            .content(null)
                            .build();
                }
            }else {
                return ResponseWrapperDTO.<OrderShippingDTO>builder()
                        .message("Para iniciar con otro pedido debe culminar el que ya inicio.")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content(null)
                        .build();
            }
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<OrderShippingDTO>builder()
                    .message("Ocurrio un error al iniciar: " + e.getMessage())
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<OrderShippingDTO> checkOrderShippingPrepared(PreparedOrderShippingDTO preparedOrderShippingDTO){
        try {
            OrderShipping orderShipping = orderShippingRepository.findById( preparedOrderShippingDTO.orderShippingId() ).orElseThrow(() -> new ResourceNotFoundException("Entrega de pedido no encontrado"));
            Order order = orderRepository.findById( orderShipping.getOrder().getId() ).orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

            if( order.getStatus() == OrderStatus.ANULADO){
                return ResponseWrapperDTO.<OrderShippingDTO>builder()
                        .message("El pedido fue anulado")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content(null)
                        .build();
            }

            if(orderShipping.getStatus() != ShippingStatus.EN_PREPARACION ){
                return ResponseWrapperDTO.<OrderShippingDTO>builder()
                        .message("El pedido debe pasar por el estado EN_PREPARACION para continuar")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content(null)
                        .build();
            }

            orderShipping.setStatus( ShippingStatus.PREPARADO );
            orderShipping.setPreparedDate(new Timestamp(new Date().getTime()));

            OrderShipping orderShippingUpdated = orderShippingRepository.save( orderShipping );
            return ResponseWrapperDTO.<OrderShippingDTO>builder()
                    .message("Pedido listo para enviar")
                    .success(true)
                    .status(HttpStatus.OK.name())
                    .content(OrderShippingDTO.parseToDTO( orderShippingUpdated ))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<OrderShippingDTO>builder()
                    .message("Ocurrio un error al cambiar de estado")
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<OrderShippingDTO> checkOrderShippingTransit(TransitOrderShippingDTO transitOrderShippingDTO){
        try {
            OrderShipping orderShipping = orderShippingRepository.findById( transitOrderShippingDTO.orderShippingId() ).orElseThrow(() -> new ResourceNotFoundException("Entrega de pedido no encontrado"));
            Carrier carrier = carrierRepository.findById( orderShipping.getCarrier().getId() ).orElseThrow(() -> new ResourceNotFoundException("Transportista no encontrado"));
            Order order = orderRepository.findById( orderShipping.getOrder().getId() ).orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

            if( order.getStatus() == OrderStatus.ANULADO){
                return ResponseWrapperDTO.<OrderShippingDTO>builder()
                        .message("El pedido fue anulado")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content(null)
                        .build();
            }

            if(orderShipping.getStatus() != ShippingStatus.PREPARADO ){
                return ResponseWrapperDTO.<OrderShippingDTO>builder()
                        .message("El pedido debe pasar por el estado PREPARADO para continuar")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content(null)
                        .build();
            }

            orderShipping.setStatus( ShippingStatus.EN_TRANSITO );
            orderShipping.setShippingDate(new Timestamp(new Date().getTime()));
            order.setStatus(OrderStatus.ENVIADO);

            carrier.setStatus(CarrierStatus.EN_RUTA);

            Carrier carrierUpdated = carrierRepository.save( carrier );
            orderShipping.setCarrier( carrierUpdated );

            orderRepository.save( order );
            OrderShipping orderShippingUpdated = orderShippingRepository.save( orderShipping );
            return ResponseWrapperDTO.<OrderShippingDTO>builder()
                    .message("Se inicio el envio del Pedido")
                    .success(true)
                    .status(HttpStatus.OK.name())
                    .content(OrderShippingDTO.parseToDTO( orderShippingUpdated ))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<OrderShippingDTO>builder()
                    .message("Ocurrio un error al iniciar")
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<OrderShippingDTO> checkOrderShippingComplete(CompleteOrderShippingDTO completeOrderShippingDTO){
        try {
            OrderShipping orderShipping = orderShippingRepository.findById( completeOrderShippingDTO.orderShippingId() ).orElseThrow(() -> new ResourceNotFoundException("Entrega de pedido no encontrado"));
            Carrier carrier = carrierRepository.findById( orderShipping.getCarrier().getId() ).orElseThrow(() -> new ResourceNotFoundException("Transportista no encontrado"));
            Order order = orderRepository.findById( orderShipping.getOrder().getId() ).orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

            if( order.getStatus() == OrderStatus.ANULADO){
                return ResponseWrapperDTO.<OrderShippingDTO>builder()
                        .message("El pedido fue anulado")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content(null)
                        .build();
            }

            if(orderShipping.getStatus() != ShippingStatus.EN_TRANSITO ){
                return ResponseWrapperDTO.<OrderShippingDTO>builder()
                        .message("El pedido debe pasar por el estado EN_TRANSITO para continuar")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content(null)
                        .build();
            }

            orderShipping.setStatus( ShippingStatus.ENTREGADO );
            orderShipping.setCompletedDate(new Timestamp(new java.util.Date().getTime()));
            order.setStatus(OrderStatus.ENTREGADO);
            order.setCompletedDate(new Timestamp(new Date().getTime()));

            carrier.setStatus(CarrierStatus.EN_DESCANSO);

            Carrier carrierUpdated = carrierRepository.save( carrier );
            orderShipping.setCarrier( carrierUpdated );

            orderRepository.save( order );
            OrderShipping orderShippingUpdated = orderShippingRepository.save( orderShipping );
            return ResponseWrapperDTO.<OrderShippingDTO>builder()
                    .message("Proceso de envio culminado, se entrego el pedido.")
                    .success(true)
                    .status(HttpStatus.OK.name())
                    .content(OrderShippingDTO.parseToDTO( orderShippingUpdated ))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<OrderShippingDTO>builder()
                    .message("Ocurrio un error al iniciar")
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<DetailedShippingOrder> findById(String orderShippingId) {
        try {
            OrderShipping orderShipping = orderShippingRepository.findById( orderShippingId ).orElseThrow(() -> new ResourceNotFoundException("Envio de pedido no encontrado"));
            return ResponseWrapperDTO.<DetailedShippingOrder>builder()
                    .message("Solicitud satisfactoria")
                    .success(true)
                    .status(HttpStatus.OK.name())
                    .content(DetailedShippingOrder.parseToDTO(orderShipping))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<DetailedShippingOrder>builder()
                    .message("Ocurrio un error: "+ e.getMessage())
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content(null)
                    .build();
        }
    }
}
