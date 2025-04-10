package com.utp.creacionesjoaquin.administration.repository;

import com.utp.creacionesjoaquin.sales.dto.dashboard.SalesByProduct;
import com.utp.creacionesjoaquin.administration.model.Product;
import com.utp.creacionesjoaquin.purchase.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    long count();
    List<Product> findBySupplier(Supplier supplier);

    @Query("SELECT NEW com.utp.creacionesjoaquin.sales.dto.dashboard.SalesByProduct(p.name, SUM(oi.amount) AS cantidadVendida)" +
            "FROM Product p LEFT JOIN OrderDetail oi ON p.id = oi.product.id " +
            "LEFT JOIN Order o ON oi.order.id = o.id " +
            "WHERE EXTRACT(YEAR FROM o.completedDate) = :year " +
            "AND EXTRACT(MONTH FROM o.completedDate) = :month " +
            "GROUP BY p.name " +
            "ORDER BY SUM(oi.amount) DESC " +
            "LIMIT :top")
    List<SalesByProduct> findTopMoreSalesProducts(
            @Param("top") int top,
            @Param("year") int year,
            @Param("month") int month
    );

    @Query("SELECT NEW com.utp.creacionesjoaquin.sales.dto.dashboard.SalesByProduct(p.name, SUM(oi.amount) AS cantidadVendida)" +
            "FROM Product p LEFT JOIN OrderDetail oi ON p.id = oi.product.id " +
            "LEFT JOIN Order o ON oi.order.id = o.id " +
            "WHERE EXTRACT(YEAR FROM o.completedDate) = :year " +
            "AND EXTRACT(MONTH FROM o.completedDate) = :month " +
            "GROUP BY p.name " +
            "ORDER BY SUM(oi.amount) ASC " +
            "LIMIT :top")
    List<SalesByProduct> findTopLowSalesProducts(
            @Param("top") int top,
            @Param("year") int year,
            @Param("month") int month
    );
}
