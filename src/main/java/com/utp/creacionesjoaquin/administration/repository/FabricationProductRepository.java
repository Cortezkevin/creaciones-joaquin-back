package com.utp.creacionesjoaquin.administration.repository;

import com.utp.creacionesjoaquin.administration.model.FabricationProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FabricationProductRepository extends JpaRepository<FabricationProduct, String> {
}
