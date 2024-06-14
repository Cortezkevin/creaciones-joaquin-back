package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.model.Grocer;
import com.utp.creacionesjoaquin.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GrocerRepository extends JpaRepository<Grocer, String> {
    Optional<Grocer> findByUser(User user);
}
