package com.utp.creacionesjoaquin.sales.service;

import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.commons.enums.OrderStatus;
import com.utp.creacionesjoaquin.order.repository.OrderDetailRepository;
import com.utp.creacionesjoaquin.order.repository.OrderRepository;
import com.utp.creacionesjoaquin.administration.repository.CategoryRepository;
import com.utp.creacionesjoaquin.administration.repository.ProductRepository;
import com.utp.creacionesjoaquin.administration.repository.SubCategoryRepository;
import com.utp.creacionesjoaquin.sales.dto.dashboard.*;
import com.utp.creacionesjoaquin.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final UserRepository userRepository;

    public ResponseWrapperDTO<List<SalesByProduct>> getTopSalesByProduct(Integer top, Integer year, Integer month, String order){
        List<SalesByProduct> topSalesProductByYear;
        if( order.equals("DESC") ){
            topSalesProductByYear = productRepository.findTopMoreSalesProducts(top, year, month);
        }else {
            topSalesProductByYear = productRepository.findTopLowSalesProducts(top, year, month);
        }
        return ResponseWrapperDTO.<List<SalesByProduct>>builder()
                .status(HttpStatus.OK.name())
                .success(true)
                .message("Solicitud satisfactoria")
                .content( topSalesProductByYear )
                .build();
    }

    public ResponseWrapperDTO<List<SalesByUser>> getTopSalesByUser(Integer top){
        List<SalesByUser> topSalesUserByYear = orderRepository.findTopSalesByUser(top);
        return ResponseWrapperDTO.<List<SalesByUser>>builder()
                .status(HttpStatus.OK.name())
                .success(true)
                .message("Solicitud satisfactoria")
                .content( topSalesUserByYear )
                .build();
    }

    public ResponseWrapperDTO<List<SalesByMonth>> getSalesMonthByYear(Integer year){
        List<SalesByMonth> salesMonthByYear = orderRepository.findVentasMensualesPorAno(year);
        return ResponseWrapperDTO.<List<SalesByMonth>>builder()
                .status(HttpStatus.OK.name())
                .success(true)
                .message("Solicitud satisfactoria")
                .content( salesMonthByYear )
                .build();
    }

    public ResponseWrapperDTO<SalesDashboard> getSales(){
        BigDecimal totalSales = orderRepository.getTotalSales();
        Integer products = (int) productRepository.count();
        Integer categories = (int) categoryRepository.count();
        Integer subcategories = (int) subCategoryRepository.count();
        Integer users = (int) userRepository.count();
        Integer orders = (int) orderRepository.count();
        Integer pendingOrders = (int) orderRepository.countByStatus(OrderStatus.PENDIENTE);
        Integer completedOrders = (int) orderRepository.countByStatus(OrderStatus.ENTREGADO);
        Integer cancelledOrders = (int) orderRepository.countByStatus(OrderStatus.ANULADO);

        Timestamp date = new Timestamp(System.currentTimeMillis());
        List<SalesByProduct> topSalesProductByYear = productRepository.findTopMoreSalesProducts(5, date.toLocalDateTime().getYear(), date.toLocalDateTime().getMonth().getValue());
        List<SalesByMonth> salesByMonth = orderRepository.findVentasMensualesPorAno(2024);
        List<SalesByUser> salesByUsers = orderRepository.findTopSalesByUser(5);

        List<OrdersCountByStatus> ordersCountByStatus = new ArrayList<>();
        ordersCountByStatus.add(new OrdersCountByStatus(OrderStatus.ENTREGADO, completedOrders.longValue()));
        ordersCountByStatus.add(new OrdersCountByStatus(OrderStatus.PENDIENTE, pendingOrders.longValue()));
        ordersCountByStatus.add(new OrdersCountByStatus(OrderStatus.ANULADO, cancelledOrders.longValue()));
        ordersCountByStatus.add(new OrdersCountByStatus(OrderStatus.EN_PROCESO, (long) (orders - (pendingOrders + cancelledOrders + completedOrders))));

        List<OrderDurationPerDistanceAVG> avgByDistanceRange = orderRepository.findAverageDurationByDistanceRange().stream().map(a -> {
            return new OrderDurationPerDistanceAVG( a[0].toString(), Double.parseDouble(a[1].toString()) );
        }).toList();

        SalesDashboard salesDashboard = new SalesDashboard(
                products,
                categories,
                subcategories,
                users,
                orders,
                totalSales,
                avgByDistanceRange,
                ordersCountByStatus,
                topSalesProductByYear,
                salesByUsers,
                salesByMonth
        );
        return ResponseWrapperDTO.<SalesDashboard>builder()
                .status(HttpStatus.OK.name())
                .success(true)
                .message("Solicitud satisfactoria")
                .content( salesDashboard )
                .build();
    }
}
