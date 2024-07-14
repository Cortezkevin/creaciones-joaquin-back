package com.utp.creacionesjoaquin.controller;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.dto.guide.*;
import com.utp.creacionesjoaquin.dto.order.InvoiceDTO;
import com.utp.creacionesjoaquin.service.GuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/guide")
@RequiredArgsConstructor
public class GuideController {

    private final GuideService guideService;

    @GetMapping("/entry")
    public ResponseEntity<ResponseWrapperDTO<List<EntryGuideDTO>>> getAllEntryGuides(){
        return ResponseEntity.ok( guideService.getAllEntryGuides() );
    }

    @GetMapping("/entry/{id}")
    public ResponseEntity<ResponseWrapperDTO<DetailedEntryGuideDTO>> getEntryGuideById(
            @PathVariable String id
    )
    {
        return ResponseEntity.ok( guideService.getEntryGuideById( id ) );
    }

    @GetMapping("/entry/pdf/{id}")
    public ResponseEntity<Resource> exportEntryGuide(
            @PathVariable String id
    ){
        InvoiceDTO invoiceDTO = guideService.exportEntryGuide( id );
        return ResponseEntity.ok()
                .contentLength(invoiceDTO.invoiceLength().longValue())
                .contentType(MediaType.APPLICATION_PDF)
                .headers(invoiceDTO.headers())
                .body(new ByteArrayResource(invoiceDTO.resource()));
    }

    @GetMapping("/exit")
    public ResponseEntity<ResponseWrapperDTO<List<ExitGuideDTO>>> getAllExitGuides(){
        return ResponseEntity.ok( guideService.getAllExitGuides() );
    }

    @GetMapping("/exit/{id}")
    public ResponseEntity<ResponseWrapperDTO<DetailedExitGuideDTO>> getExitGuideById(
            @PathVariable String id
    )
    {
        return ResponseEntity.ok( guideService.getExitGuideById( id ) );
    }

    @GetMapping("/exit/pdf/{id}")
    public ResponseEntity<Resource> exportExitGuide(
            @PathVariable String id
    ){
        InvoiceDTO invoiceDTO = guideService.exportExitGuide( id );
        return ResponseEntity.ok()
                .contentLength(invoiceDTO.invoiceLength().longValue())
                .contentType(MediaType.APPLICATION_PDF)
                .headers(invoiceDTO.headers())
                .body(new ByteArrayResource(invoiceDTO.resource()));
    }

    @GetMapping("/rejection")
    public ResponseEntity<ResponseWrapperDTO<List<RejectionGuideDTO>>> getAllRejectionGuides(){
        return ResponseEntity.ok( guideService.getAllRejectionGuides() );
    }
}
