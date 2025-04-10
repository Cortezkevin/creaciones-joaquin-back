package com.utp.creacionesjoaquin.purchase.repository;

import com.utp.creacionesjoaquin.purchase.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String> {
}
