package com.utp.creacionesjoaquin.administration.repository;

import com.utp.creacionesjoaquin.administration.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, String> {
}
