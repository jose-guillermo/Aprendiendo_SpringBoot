package com.jose.curso.springboot.app.springboot_crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jose.curso.springboot.app.springboot_crud.Exceptions.ResourceNotFoundException;
import com.jose.curso.springboot.app.springboot_crud.dto.ProductDto;
import com.jose.curso.springboot.app.springboot_crud.dto.ProductUpdateDto;
import com.jose.curso.springboot.app.springboot_crud.entities.Product;
import com.jose.curso.springboot.app.springboot_crud.mappers.ProductMapper;
import com.jose.curso.springboot.app.springboot_crud.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductMapper productMapper;

    @Transactional(readOnly = true)
    @Override
    // public List<Product> findAll() {
    //     return (List<Product>) repository.findAll();
    // }
     public List<ProductDto> findAll() {
        List<Product> products = (List<Product>) repository.findAll();
        return products.stream().map(productMapper::productToProductDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    // public Optional<Product> findById(Long id) {
    //     return repository.findById(id);
    // }
    public ProductDto findById(Long id) {
        Optional<Product> product = repository.findById(id);
        if(product.isPresent()){
            return productMapper.productToProductDto(product.get());
        } else {
            throw new ResourceNotFoundException("El producto con id " + id + " no existe");
        }
    }

    @Transactional
    @Override
    // public Product save(Product product) {
    //     return repository.save(product);
    // }
    public ProductDto save(ProductDto productDto) {
        Product product = productMapper.productDtoToProduct(productDto);

        return productMapper.productToProductDto(repository.save(product));
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
    public ProductDto update(Long id, ProductUpdateDto productDto) {
        Product product = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto con id " + id + " no encontrado"));

        productMapper.updateProductFromDto(productDto, product);
        
        return productMapper.productToProductDto(repository.save(product));
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
        return productMapper.productToProductDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySku(String sku) {
        return repository.existsBySku(sku);
    }

}
