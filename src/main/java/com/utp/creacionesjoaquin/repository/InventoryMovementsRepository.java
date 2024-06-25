package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.model.InventoryMovements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryMovementsRepository extends JpaRepository<InventoryMovements, String> {
}
