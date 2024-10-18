package com.krios.pro.ProductRepository;

import com.krios.pro.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PdfRepository extends JpaRepository<Product, Integer> {
}
