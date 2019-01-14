package com.example.springpdfexcel.repositories;

import com.example.springpdfexcel.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
