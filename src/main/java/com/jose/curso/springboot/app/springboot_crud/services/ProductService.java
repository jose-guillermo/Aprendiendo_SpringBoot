package com.jose.curso.springboot.app.springboot_crud.services;

import java.util.List;
// import java.util.Optional;

import com.jose.curso.springboot.app.springboot_crud.dto.ProductDto;
// import com.jose.curso.springboot.app.springboot_crud.entities.Product;
import com.jose.curso.springboot.app.springboot_crud.dto.ProductUpdateDto;

public interface ProductService {
    // List<Product> findAll();
    List<ProductDto> findAll();

    // Optional<Product> findById(Long id);
    ProductDto findById(Long id);

    // Product save(Product product);
    ProductDto save(ProductDto product);

    // Optional<Product> update(Long id, Product product);
    ProductDto update(Long id, ProductUpdateDto product);

    // Optional<Product> delete(Long id);
    ProductDto delete(Long id);
    
    boolean existsBySku(String sku);
}
