package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.model.Product;
import com.utp.creacionesjoaquin.model.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImagesRepository extends JpaRepository<ProductImages, String> {
    List<ProductImages> findByProduct(Product product);
    long countByProduct(Product product);
}
