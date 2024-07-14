package com.utp.creacionesjoaquin.service;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.fabrication.CreateFabricationProductDTO;
import com.utp.creacionesjoaquin.dto.fabrication.FabricationProductDTO;
import com.utp.creacionesjoaquin.enums.FabricationStatus;
import com.utp.creacionesjoaquin.enums.OperatorStatus;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.model.FabricationProduct;
import com.utp.creacionesjoaquin.model.Operator;
import com.utp.creacionesjoaquin.model.Product;
import com.utp.creacionesjoaquin.repository.FabricationProductRepository;
import com.utp.creacionesjoaquin.repository.OperatorRepository;
import com.utp.creacionesjoaquin.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FabricationProductService {
    private final FabricationProductRepository fabricationProductRepository;
    private final ProductRepository productRepository;
    private final OperatorRepository operatorRepository;

    public ResponseWrapperDTO<FabricationProductDTO> create(CreateFabricationProductDTO createFabricationProductDTO){
        try {
            Product product = productRepository.findById( createFabricationProductDTO.productId() ).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            List<Operator> operatorsToFabrication = new ArrayList<>();
            if( createFabricationProductDTO.operatorsIds() != null){
                createFabricationProductDTO.operatorsIds().forEach( oid -> {
                    operatorRepository.findById(oid).ifPresent(o -> {
                        if( o.getStatus().equals(OperatorStatus.DISPONIBLE) ){
                            o.setStatus( OperatorStatus.EN_ESPERA );
                            operatorsToFabrication.add( o );
                        }
                    });
                });
                if( operatorsToFabrication.size() < 2 ){
                    throw new RuntimeException("Algunos o todos los operadores seleccionados no estan disponibles");
                }
            }else {
                List<Operator> availableOperators = operatorRepository.findByStatus( OperatorStatus.DISPONIBLE );
                if( availableOperators.size() < 2 ){
                    throw new RuntimeException("No hay operadores disponibles para fabricar este producto");
                }else {
                    Operator operator1 = availableOperators.get(0);
                    Operator operator2 = availableOperators.get(1);
                    operator1.setStatus(OperatorStatus.EN_ESPERA);
                    operator2.setStatus(OperatorStatus.EN_ESPERA);
                    operatorsToFabrication.add( operator1 );
                    operatorsToFabrication.add( operator2 );
                }
            }

            List<Operator> operatorsUpdated = operatorRepository.saveAll( operatorsToFabrication );

            FabricationProduct fabricationProduct = FabricationProduct.builder()
                    .product( product )
                    .createdDate(new Timestamp(System.currentTimeMillis()))
                    .status(FabricationStatus.PENDIENTE)
                    .operators(operatorsUpdated)
                    .build();

            FabricationProduct fabricationProductCreated = fabricationProductRepository.save( fabricationProduct );
            return ResponseWrapperDTO.<FabricationProductDTO>builder()
                    .message("Orden de Producto creado")
                    .success(true)
                    .status(HttpStatus.OK.name())
                    .content(FabricationProductDTO.parseToDTO( fabricationProductCreated ))
                    .build();
        }catch ( ResourceNotFoundException e ){
            return ResponseWrapperDTO.<FabricationProductDTO>builder()
                    .message(e.getMessage())
                    .success(false)
                    .status(HttpStatus.NOT_FOUND.name())
                    .content(null)
                    .build();
        }
    }

    public ResponseWrapperDTO<List<FabricationProductDTO>> getAll(){
        Sort sort = Sort.by(Sort.Direction.DESC, "startDate");
        List<FabricationProductDTO> fabricationProductDTOList = fabricationProductRepository.findAll( sort ).stream().map(FabricationProductDTO::parseToDTO).toList();
        return ResponseWrapperDTO.<List<FabricationProductDTO>>builder()
                .message("Solicitud satisfactoria")
                .status(HttpStatus.OK.name())
                .success(true)
                .content( fabricationProductDTOList )
                .build();
    }
}
