package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.model.ProductMaterials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMaterialsRepository extends JpaRepository<ProductMaterials, String> {
}
