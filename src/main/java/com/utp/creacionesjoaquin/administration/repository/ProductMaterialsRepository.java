package com.utp.creacionesjoaquin.administration.repository;

import com.utp.creacionesjoaquin.administration.model.ProductMaterials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMaterialsRepository extends JpaRepository<ProductMaterials, String> {
}
