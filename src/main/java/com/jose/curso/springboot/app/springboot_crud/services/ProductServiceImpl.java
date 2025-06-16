package com.jose.curso.springboot.app.springboot_crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jose.curso.springboot.app.springboot_crud.Exceptions.ResourceNotFoundException;
import com.jose.curso.springboot.app.springboot_crud.dto.ProductDto;
import com.jose.curso.springboot.app.springboot_crud.entities.Product;
import com.jose.curso.springboot.app.springboot_crud.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    @Override
    // public List<Product> findAll() {
    //     return (List<Product>) repository.findAll();
    // }
     public List<ProductDto> findAll() {
        List<Product> products = (List<Product>) repository.findAll();
        return products.stream().map(ProductDto::new).toList();
    }

    @Transactional(readOnly = true)
    @Override
    // public Optional<Product> findById(Long id) {
    //     return repository.findById(id);
    // }
    public ProductDto findById(Long id) {
        Optional<Product> product = repository.findById(id);
        if(product.isPresent()){
            return new ProductDto(product.get());
        } else {
            throw new ResourceNotFoundException("Usuario con id " + id + "no encontrado");
        }
    }

    @Transactional
    @Override
    // public Product save(Product product) {
    //     return repository.save(product);
    // }
    public ProductDto save(ProductDto productDto) {
        Product product = new Product();
        product.setDescription(productDto.getDescription());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setSku(productDto.getSku());

        return new ProductDto(repository.save(product));
    }

    @Transactional
    @Override
    // public Optional<Product> update(Long id, Product product) {
    //     Optional<Product> productOptional = repository.findById(id);

    //     if(productOptional.isPresent()){
    //         product.setId(id);
    //         return Optional.of(repository.save(product));
    //     }
        
    //     return productOptional;
    // }
    public ProductDto update(Long id, ProductDto productDto) {
        Product product = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto con id " + id + " no encontrado"));

        product.setDescription(productDto.getDescription());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setSku(productDto.getSku());
        
        return new ProductDto(repository.save(product));
    }

    @Transactional
    @Override
    // public Optional<Product> delete(Long id) {
    //     Optional<Product> productOptional = repository.findById(id);
    //     productOptional.ifPresent(productDb -> {
    //         repository.delete(productDb);
    //     });
    //     return productOptional;
    // }
    public ProductDto delete(Long id) {
        Product product = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto con id " + id + " no encontrado"));
        repository.delete(product);
        return new ProductDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySku(String sku) {
        return repository.existsBySku(sku);
    }

}
