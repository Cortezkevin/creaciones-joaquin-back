package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.model.ExitGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExitGuideRepository extends JpaRepository<ExitGuide, String> {
}
