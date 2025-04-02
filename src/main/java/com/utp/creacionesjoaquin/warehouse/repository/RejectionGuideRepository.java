package com.utp.creacionesjoaquin.warehouse.repository;

import com.utp.creacionesjoaquin.warehouse.model.RejectionGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RejectionGuideRepository extends JpaRepository<RejectionGuide, String> {
}
