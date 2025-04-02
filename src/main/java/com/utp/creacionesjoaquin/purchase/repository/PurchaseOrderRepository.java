package com.utp.creacionesjoaquin.purchase.repository;

import com.utp.creacionesjoaquin.purchase.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, String> {
}
