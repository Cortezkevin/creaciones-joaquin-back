package com.utp.creacionesjoaquin.administration.repository;

import com.utp.creacionesjoaquin.administration.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
}
