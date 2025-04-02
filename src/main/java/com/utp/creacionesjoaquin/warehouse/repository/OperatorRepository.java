package com.utp.creacionesjoaquin.warehouse.repository;

import com.utp.creacionesjoaquin.commons.enums.OperatorStatus;
import com.utp.creacionesjoaquin.warehouse.model.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, String> {
    List<Operator> findByStatus(OperatorStatus status);
}
