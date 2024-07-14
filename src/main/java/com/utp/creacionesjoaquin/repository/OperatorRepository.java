package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.enums.OperatorStatus;
import com.utp.creacionesjoaquin.model.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, String> {
    List<Operator> findByStatus(OperatorStatus status);
}
