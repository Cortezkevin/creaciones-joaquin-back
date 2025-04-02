package com.utp.creacionesjoaquin.warehouse.repository;

import com.utp.creacionesjoaquin.warehouse.model.Carrier;
import com.utp.creacionesjoaquin.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarrierRepository extends JpaRepository<Carrier, String> {
    Optional<Carrier> findByUser(User user);
}
