package com.jose.curso.springboot.app.springboot_crud.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
// import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import com.jose.curso.springboot.app.springboot_crud.dto.ProductDto;
import com.jose.curso.springboot.app.springboot_crud.dto.ProductUpdateDto;
// import com.jose.curso.springboot.app.springboot_crud.ProductValidation;
// import com.jose.curso.springboot.app.springboot_crud.entities.Product;
import com.jose.curso.springboot.app.springboot_crud.services.ProductService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(originPatterns = "*", origins = "https://localhost:4200")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService service;

    // @Autowired
    // private ProductValidation validation;

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    // public List<Product> list(){
    public List<ProductDto> list(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    // public ResponseEntity<?> view(@PathVariable Long id){
    //     Optional<Product> productOptional = service.findById(id);
    //     if(productOptional.isPresent()) {
    //         return ResponseEntity.ok(productOptional.get());
    //     }
    //     return ResponseEntity.notFound().build();
    // }
    public ResponseEntity<?> view(@PathVariable Long id){
        ProductDto productDto = service.findById(id);
        return ResponseEntity.ok(productDto);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<?> create(@Valid @RequestBody Product product, BindingResult result) {
    //     if (result.hasFieldErrors()) {
    //         return validation(result);
    //     }
    //     return ResponseEntity.status(HttpStatus.CREATED).body(service.save(product));
    // }
    public ResponseEntity<?> create(@Valid @RequestBody ProductDto productDto, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(productDto));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<?> update(@Valid @RequestBody Product product, BindingResult result, @PathVariable Long id) {
    //     // validation.validate(product, result);
    //     if (result.hasFieldErrors()) {
    //         return validation(result);
    //     }
    //     Optional<Product> productOptional = service.update(id, product);
    //     if(productOptional.isPresent()){
    //         return ResponseEntity.status(HttpStatus.CREATED).body(productOptional.get());
    //     }
    //     return ResponseEntity.notFound().build();
    // }
    public ResponseEntity<?> update(@Valid @RequestBody ProductUpdateDto productDto, BindingResult result, @PathVariable Long id) {
        // validation.validate(product, result);
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        return ResponseEntity.ok(service.update(id, productDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<?> delete(@PathVariable Long id){
    //     Optional<Product> productOptional = service.delete(id);
    //     if(productOptional.isPresent()) {
    //         return ResponseEntity.ok(productOptional.get());
    //     }
    //     return ResponseEntity.notFound().build();
    // }
    public ResponseEntity<?> delete(@PathVariable Long id){
        return ResponseEntity.ok(service.delete(id));
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
