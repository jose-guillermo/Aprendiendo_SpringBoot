package com.jose.curso.springboot.app.springboot_crud.services;

import java.util.List;

import com.jose.curso.springboot.app.springboot_crud.dto.UserDto;
// import com.jose.curso.springboot.app.springboot_crud.entities.User;

public interface UserService {

    // List<User> findAll();
    List<UserDto> findAll();

    UserDto findByUsername(String username);

    // User save(User user);
    UserDto save(UserDto user);

    boolean existsByUsername(String username);
}
