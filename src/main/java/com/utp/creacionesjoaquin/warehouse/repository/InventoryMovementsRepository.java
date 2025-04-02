package com.utp.creacionesjoaquin.warehouse.repository;

import com.utp.creacionesjoaquin.warehouse.model.InventoryMovements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryMovementsRepository extends JpaRepository<InventoryMovements, String> {
}
