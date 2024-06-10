package com.utp.creacionesjoaquin.repository;

import com.utp.creacionesjoaquin.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,String> {
}
