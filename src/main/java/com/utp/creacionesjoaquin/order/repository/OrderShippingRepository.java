package com.utp.creacionesjoaquin.order.repository;

import com.utp.creacionesjoaquin.order.model.Order;
import com.utp.creacionesjoaquin.order.model.OrderShipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderShippingRepository extends JpaRepository<OrderShipping, String> {
    Optional<OrderShipping> findByOrder(Order order);
}
