package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.model.Order;
import com.utp.creacionesjoaquin.model.OrderPreparation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderPreparationRepository extends JpaRepository<OrderPreparation, String> {
    Optional<OrderPreparation> findByOrder(Order order);
}
