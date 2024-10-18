 package com.krios.pro.ProductRepository;
 import com.krios.pro.Entity.Customer;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.stereotype.Repository;

 @Repository
 public interface CustomerRepository extends JpaRepository<Customer, Long> {

 }
