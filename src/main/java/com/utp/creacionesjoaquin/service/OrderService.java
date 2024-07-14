package com.utp.creacionesjoaquin.service;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.UpdateDates;
import com.utp.creacionesjoaquin.dto.order.DetailedOrderDTO;
import com.utp.creacionesjoaquin.dto.order.InvoiceDTO;
import com.utp.creacionesjoaquin.dto.order.OrderDTO;
import com.utp.creacionesjoaquin.enums.OrderStatus;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.model.Order;
import com.utp.creacionesjoaquin.repository.OrderRepository;
import com.utp.creacionesjoaquin.security.model.User;
import com.utp.creacionesjoaquin.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public ResponseWrapperDTO<DetailedOrderDTO> findById(String orderId){
        try {
            Order order = orderRepository.findById( orderId ).orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));
            return ResponseWrapperDTO.<DetailedOrderDTO>builder()
                    .success(true)
                    .message("Solicitud Satisfactoria")
                    .status(HttpStatus.OK.name())
                    .content(DetailedOrderDTO.parseToDTO( order ))
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<DetailedOrderDTO>builder()
                    .message("Ocurrio un error: " + e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success(false)
                    .content( null )
                    .build();
        }
    }

    public ResponseWrapperDTO<List<OrderDTO>> findAll(){
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        List<OrderDTO> orderDTOList = orderRepository.findAll(sort).stream().map( OrderDTO::parseToDTO ).toList();
        return ResponseWrapperDTO.<List<OrderDTO>>builder()
                .message("Solicitud Satisfactoria")
                .status(HttpStatus.OK.name())
                .success(true)
                .content( orderDTOList )
                .build();
    }

    public ResponseWrapperDTO<List<OrderDTO>> findByUser(String userId) {
        try {
            Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            List<OrderDTO> orderDTOList = orderRepository.findByUser( user, sort ).stream().map( OrderDTO::parseToDTO ).toList();
            return ResponseWrapperDTO.<List<OrderDTO>>builder()
                    .message("Solicitud Satisfactoria")
                    .status(HttpStatus.OK.name())
                    .success(true)
                    .content( orderDTOList )
                    .build();
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<List<OrderDTO>>builder()
                    .message("Ocurrio un error: " + e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success(false)
                    .content( null )
                    .build();
        }
    }

    public ResponseWrapperDTO<OrderDTO> cancelOrder(String orderId){
        try {
            Order order = orderRepository.findById( orderId ).orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));
            if( order.getStatus().equals(OrderStatus.EN_PROCESO) || order.getStatus().equals(OrderStatus.PENDIENTE)){
                order.setStatus( OrderStatus.ANULADO );
                Order orderUpdated = orderRepository.save(order);
                return ResponseWrapperDTO.<OrderDTO>builder()
                        .message("Pedido anulado correctamente")
                        .status(HttpStatus.OK.name())
                        .success(true)
                        .content( OrderDTO.parseToDTO( orderUpdated ) )
                        .build();
            }else {
                return ResponseWrapperDTO.<OrderDTO>builder()
                        .message("Ya no puede anular el pedido")
                        .status(HttpStatus.BAD_REQUEST.name())
                        .success(false)
                        .content( null )
                        .build();
            }
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<OrderDTO>builder()
                    .message("Ocurrio un error: " + e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .success(false)
                    .content( null )
                    .build();
        }
    }

    public InvoiceDTO exportInvoice(String orderId) {
       try {
           Order order = orderRepository.findById( orderId ).orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));
           File file = ResourceUtils.getFile("classpath:orderInvoice.jasper");
           File imgLogo = ResourceUtils.getFile("classpath:static/LOGO.jpeg");
           JasperReport report = ( JasperReport ) JRLoader.loadObject(file);

           HashMap<String,Object> params = new HashMap<>();
           params.put("logoEmpresa", new FileInputStream( imgLogo ));
           params.put("fullName",order.getUser().getPersonalInformation().getFullName());
           params.put("subtotal",order.getSubtotal());
           params.put("delivery",order.getShippingCost());
           params.put("discount",order.getDiscount());
           params.put("tax",order.getTax());
           params.put("total",order.getTotal());
           params.put("ds",new JRBeanCollectionDataSource(order.getOrderDetails()));

           JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, new JREmptyDataSource());

           byte[] invoice = JasperExportManager.exportReportToPdf( jasperPrint );
           String sdf = (new SimpleDateFormat("dd/MM/yyyy")).format(new Date());
           StringBuilder stringBuilder = new StringBuilder().append("invoice:");

           ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                   .filename(stringBuilder.append(order.getId()).append("generatedDate:").append(sdf).append(".pdf").toString())
                   .build();

           HttpHeaders headers = new HttpHeaders();
           headers.setContentDisposition( contentDisposition );

           return new InvoiceDTO(
                   invoice.length,
                   invoice,
                   headers
           );
       }catch (ResourceNotFoundException e){
           System.out.println("ERROR: "+ e.getMessage() );
           return null;
       } catch (FileNotFoundException e) {
           System.out.println("ERROR: "+ e.getMessage() );
           return null;
       } catch (JRException e) {
           System.out.println("ERROR: "+ e.getMessage() );
           return null;
       }
    }

    public String updateDates(UpdateDates updateDates){
        try {
            Order order = orderRepository.findById( updateDates.orderId() ).orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));
            order.setCreatedDate( updateDates.createdDate() );
            order.setDistance(updateDates.distance() );
            order.getOrderPreparation().setCreatedDate( updateDates.createdDate() );
            order.getOrderPreparation().setStartDate( updateDates.createdDate() );
            order.getOrderPreparation().setPreparedDate( updateDates.createdDate() );

            order.getOrderPreparation().setCompletedDate( updateDates.shippingDate() );
            order.getOrderShipping().setStartDate( updateDates.shippingDate() );
            order.getOrderShipping().setPreparedDate( updateDates.shippingDate() );
            order.getOrderShipping().setShippingDate( updateDates.shippingDate() );
            order.getOrderShipping().setCreatedDate( updateDates.shippingDate() );
            order.getOrderShipping().setDistance(updateDates.distance() );

            order.getOrderShipping().setCompletedDate( updateDates.completedDate() );
            order.setCompletedDate( updateDates.completedDate() );

            order.getExitGuide().setDate( updateDates.shippingDate() );
            order.getExitGuide().setInventoryMovementsList(order.getExitGuide().getInventoryMovementsList().stream().map(im -> {
                im.setDate( updateDates.shippingDate() );
                return im;
            }).toList());
            orderRepository.save(order);
            return "Fechas actualizadas";
        }catch (ResourceNotFoundException e){
            System.out.println("ERROR: "+ e.getMessage() );
            return "OCurrio un error";
        }
    }
}
