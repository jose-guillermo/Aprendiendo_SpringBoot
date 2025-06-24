package com.jose.curso.springboot.app.springboot_crud.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.jose.curso.springboot.app.springboot_crud.dto.ProductDto;
import com.jose.curso.springboot.app.springboot_crud.dto.ProductUpdateDto;
import com.jose.curso.springboot.app.springboot_crud.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product productDtoToProduct(ProductDto productDto);

    ProductDto productToProductDto(Product product);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromDto(ProductUpdateDto dto, @MappingTarget Product product);
}
