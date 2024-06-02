package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.model.Product;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    long count();
}
