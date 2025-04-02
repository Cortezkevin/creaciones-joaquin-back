package com.utp.creacionesjoaquin.purchase.repository;

import com.utp.creacionesjoaquin.purchase.model.PurchaseOrderReception;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderReceptionRepository extends JpaRepository<PurchaseOrderReception, String> {
}
