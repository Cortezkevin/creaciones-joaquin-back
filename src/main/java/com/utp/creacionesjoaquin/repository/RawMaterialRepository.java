package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.model.RawMaterial;
import com.utp.creacionesjoaquin.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterial, String> {
    List<RawMaterial> findBySupplier(Supplier supplier);
}
