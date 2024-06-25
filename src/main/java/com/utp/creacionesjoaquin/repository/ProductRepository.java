package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.model.Product;
import com.utp.creacionesjoaquin.model.Supplier;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    long count();
    List<Product> findBySupplier(Supplier supplier);
}
