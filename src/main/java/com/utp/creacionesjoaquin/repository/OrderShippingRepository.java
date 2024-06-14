package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.model.Order;
import com.utp.creacionesjoaquin.model.OrderShipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderShippingRepository extends JpaRepository<OrderShipping, String> {
    Optional<OrderShipping> findByOrder(Order order);
}
