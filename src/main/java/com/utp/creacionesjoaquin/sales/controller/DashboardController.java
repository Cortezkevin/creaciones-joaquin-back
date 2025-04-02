package com.utp.creacionesjoaquin.sales.controller;

import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.sales.dto.dashboard.SalesByMonth;
import com.utp.creacionesjoaquin.sales.dto.dashboard.SalesByProduct;
import com.utp.creacionesjoaquin.sales.dto.dashboard.SalesByUser;
import com.utp.creacionesjoaquin.sales.dto.dashboard.SalesDashboard;
import com.utp.creacionesjoaquin.sales.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/sales")
    public ResponseEntity<ResponseWrapperDTO<SalesDashboard>> getSales(){
        return ResponseEntity.ok( dashboardService.getSales() );
    }

    @GetMapping("/sales/month/{year}")
    public ResponseEntity<ResponseWrapperDTO<List<SalesByMonth>>> getSalesMonthByYear(
            @PathVariable Integer year
    ){
        return ResponseEntity.ok( dashboardService.getSalesMonthByYear( year) );
    }


    @GetMapping("/sales/product")
    public ResponseEntity<ResponseWrapperDTO<List<SalesByProduct>>> getSalesTopByProduct(
            @RequestParam("top") Integer top,
            @RequestParam("year") Integer year,
            @RequestParam("month") Integer month,
            @RequestParam("order") String order
    ){
        return ResponseEntity.ok( dashboardService.getTopSalesByProduct( top, year, month, order ) );
    }

    @GetMapping("/sales/user/{top}")
    public ResponseEntity<ResponseWrapperDTO<List<SalesByUser>>> getSalesTopByUser(
            @PathVariable Integer top
    ){
        return ResponseEntity.ok( dashboardService.getTopSalesByUser( top ) );
    }

}

