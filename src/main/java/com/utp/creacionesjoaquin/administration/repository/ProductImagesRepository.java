package com.utp.creacionesjoaquin.administration.repository;

import com.utp.creacionesjoaquin.administration.model.Product;
import com.utp.creacionesjoaquin.administration.model.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImagesRepository extends JpaRepository<ProductImages, String> {
    List<ProductImages> findByProduct(Product product);
    long countByProduct(Product product);
}
