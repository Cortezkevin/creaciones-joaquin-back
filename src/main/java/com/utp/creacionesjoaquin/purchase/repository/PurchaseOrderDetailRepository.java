package com.utp.creacionesjoaquin.purchase.repository;

import com.utp.creacionesjoaquin.purchase.model.PurchaseOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetail, String> {
}
