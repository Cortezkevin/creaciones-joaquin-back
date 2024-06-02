package com.utp.creacionesjoaquin.security.repository;

import com.utp.creacionesjoaquin.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email );
    Boolean existsByEmail(String email );
    Optional<User> findByTokenPassword(String token);
}
