package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, String> {
}
