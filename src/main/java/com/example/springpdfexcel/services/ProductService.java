package com.example.springpdfexcel.services;

import com.example.springpdfexcel.entities.Product;

public interface ProductService {

    Iterable<Product> findAll();

    Product find(long id);
}
