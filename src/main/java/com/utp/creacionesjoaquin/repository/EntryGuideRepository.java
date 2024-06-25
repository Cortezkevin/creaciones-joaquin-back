package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.model.EntryGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryGuideRepository extends JpaRepository<EntryGuide, String> {
}
