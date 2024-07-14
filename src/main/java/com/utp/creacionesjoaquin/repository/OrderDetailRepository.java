package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.dto.dashboard.SalesByProduct;
import com.utp.creacionesjoaquin.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    @Query("SELECT NEW com.utp.creacionesjoaquin.dto.dashboard.SalesByProduct(p.name, SUM(od.amount)) "+
            "FROM OrderDetail od " +
            "JOIN od.product p " +
            "JOIN od.order o " +
            "WHERE o.status = 'ENTREGADO' " +
            "GROUP BY p.name " +
            "ORDER BY SUM(od.amount) DESC " +
            "LIMIT :top")
    List<SalesByProduct> getSalesAmountProducts(@Param("top") int top);
}
