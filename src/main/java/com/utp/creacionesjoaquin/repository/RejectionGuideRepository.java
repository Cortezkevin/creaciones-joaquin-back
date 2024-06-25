package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.model.RejectionGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RejectionGuideRepository extends JpaRepository<RejectionGuide, String> {
}
