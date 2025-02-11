package com.springboot.ezenbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.ezenbackend.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    
}
