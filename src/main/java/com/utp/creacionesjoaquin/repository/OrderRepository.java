package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.model.Order;
import com.utp.creacionesjoaquin.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUser(User user);
}
