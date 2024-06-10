package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
}
