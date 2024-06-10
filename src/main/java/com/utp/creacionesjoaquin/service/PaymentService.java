package com.utp.creacionesjoaquin.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.utp.creacionesjoaquin.cloudinary.dto.UploadDTO;
import com.utp.creacionesjoaquin.cloudinary.service.CloudinaryService;
import com.utp.creacionesjoaquin.dto.OrderCreatedDTO;
import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.order.InvoiceDTO;
import com.utp.creacionesjoaquin.dto.payment.PaymentIndentResponseDTO;
import com.utp.creacionesjoaquin.dto.product.UploadResultDTO;
import com.utp.creacionesjoaquin.enums.OrderStatus;
import com.utp.creacionesjoaquin.enums.PaymentMethod;
import com.utp.creacionesjoaquin.enums.ShippingStatus;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;

import com.utp.creacionesjoaquin.model.*;
import com.utp.creacionesjoaquin.repository.InvoiceRepository;
import com.utp.creacionesjoaquin.repository.OrderDetailRepository;
import com.utp.creacionesjoaquin.repository.OrderRepository;
import com.utp.creacionesjoaquin.security.model.User;
import com.utp.creacionesjoaquin.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.hibernate.validator.constraints.Email;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    public ResponseWrapperDTO<String> successPayment(String userId){
        try {
            User user = userRepository.findById( userId ).orElseThrow(() -> new ResourceNotFoundException(("Usuario no encontrado")));
            Address userAddress= user.getPersonalInformation().getAddress();
            Cart userCart = user.getCart();

            java.util.Date createdDate = new java.util.Date();

            Order newOrder = Order.builder()
                    .address( userAddress.getFullAddress() )
                    .paymentMethod(PaymentMethod.TARJETA)
                    .note("GAAA")
                    .tax( userCart.getTax() )
                    .total( userCart.getTotal() )
                    .subtotal( userCart.getSubtotal() )
                    .discount( userCart.getDiscount() )
                    .shippingCost( user.getCart().getShippingCost())
                    .shippingStatus( ShippingStatus.EN_PREPARACION )
                    .createdDate( new Date(createdDate.getTime()).toLocalDate())
                    .status( OrderStatus.PENDIENTE )
                    .user( user )
                    .build();

            Order orderCreated = orderRepository.save( newOrder );

            List<OrderDetail> orderDetailList = new ArrayList<>();
            userCart.getCartItems().forEach(cartItem -> {
                OrderDetail newOrderDetail = OrderDetail.builder()
                        .order( orderCreated )
                        .amount( cartItem.getAmount() )
                        .price( cartItem.getProduct().getPrice())
                        .total(cartItem.getTotal())
                        .name( cartItem.getProduct().getName())
                        .product( cartItem.getProduct() )
                        .build();
                orderDetailList.add( newOrderDetail );
            });
            orderDetailRepository.saveAll( orderDetailList );
            orderCreated.setOrderDetails(orderDetailList);

            orderRepository.save(orderCreated);

            log.info("CALL GENERATE UPLOAD PDF");
            String invoicePDFUrl = generateAndUploadInvoicePDF( orderCreated.getId() );

            Invoice newInvoice = Invoice.builder()
                    .order( orderCreated )
                    .url(invoicePDFUrl)
                    .issuedDate( new Date(createdDate.getTime()).toLocalDate() )
                    .build();

            invoiceRepository.save( newInvoice );

            return ResponseWrapperDTO.<String>builder()
                    .status(HttpStatus.OK.name())
                    .success( true )
                    .content( null )
                    .message( "Compra realizada correctamente"  )
                    .build();
        }catch (Exception e){
            return ResponseWrapperDTO.<String>builder()
                    .status(HttpStatus.OK.name())
                    .success( false )
                    .content( null )
                    .message( "Ocurrio un error: " + e.getMessage() )
                    .build();
        }
    }

    public ResponseWrapperDTO<PaymentIndentResponseDTO> createIndent(String userId){
        try {
            User user = userRepository.findById( userId ).orElseThrow(() -> new ResourceNotFoundException(("Usuario no encontrado")));
            Stripe.apiKey = "sk_test_51LDHfGCjrtAyA6AHlTaXE88uQjaFPSq0EHYWGbsCIiELO6Jt1n1v8PGBPtl4PRlZrOSpl5gK8XC3xTsiusbZqP8D00sPgDAJA2";

            Integer totalInt = user.getCart().getTotal().intValue();
            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(totalInt.longValue() * 100)
                            .setCurrency("pen")
                            .setAutomaticPaymentMethods(
                                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
                            )
                            .setShipping(PaymentIntentCreateParams.Shipping.builder()
                                    .setCarrier("Juan Alberto")
                                    .setName(user.getPersonalInformation().getFullName())
                                    .setPhone(user.getPersonalInformation().getPhone())
                                    .setAddress( PaymentIntentCreateParams.Shipping.Address.builder()
                                            .setCity( user.getPersonalInformation().getAddress().getDistrict() )
                                            .setLine1( user.getPersonalInformation().getAddress().getStreet() )
                                            .setCountry("Peru")
                                            .setPostalCode( user.getPersonalInformation().getAddress().getPostalCode().toString())
                                    .build())
                            .build())
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return ResponseWrapperDTO.<PaymentIndentResponseDTO>builder()
                            .status(HttpStatus.OK.name())
                            .success( true )
                            .content( new PaymentIndentResponseDTO( paymentIntent.getClientSecret() ) )
                            .message( "Secreto del client generado" )
                            .build();
        }catch (StripeException | ResourceNotFoundException e) {
            return ResponseWrapperDTO.<PaymentIndentResponseDTO>builder()
                    .status(HttpStatus.OK.name())
                    .success( false )
                    .content( null )
                    .message( "Ocurrio un error: " + e.getMessage() )
                    .build();
        }
    }

    public String generateAndUploadInvoicePDF(String orderId){
        try {
            Order order = orderRepository.findById( orderId ).orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));
            ClassPathResource jasperResource = new ClassPathResource("/orderInvoice.jasper");
            ClassPathResource logoResource = new ClassPathResource("/static/LOGO.jpeg");
            log.info("LOADING CLASSPATH FILES");
            File file = jasperResource.getFile(); // ResourceUtils.getFile( jasperResource.getURL() );//ResourceUtils.getFile("classpath:orderInvoice.jasper");
            File imgLogo = logoResource.getFile();//ResourceUtils.getFile("classpath:static/LOGO.jpeg");
            log.info("CLASSPATH FILES loaded");
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

            Path tempPDFPath = Files.createTempFile("invoice_"+order.getId(), ".pdf");
            File tempPDFFile = tempPDFPath.toFile();

            JasperExportManager.exportReportToPdfStream( jasperPrint, new FileOutputStream(tempPDFFile));

            UploadResultDTO uploadResultDTO = cloudinaryService.upload("pdf/"+order.getUser().getId(), new UploadDTO(tempPDFFile,"invoice_"+order.getId()));
            log.info("UPLOAD DTO " + uploadResultDTO.url());
            return uploadResultDTO.url();

        }catch (ResourceNotFoundException e){
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        } catch (JRException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
