package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, String> {
}
