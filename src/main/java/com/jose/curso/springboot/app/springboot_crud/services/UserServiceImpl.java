package com.jose.curso.springboot.app.springboot_crud.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jose.curso.springboot.app.springboot_crud.Exceptions.ResourceNotFoundException;
import com.jose.curso.springboot.app.springboot_crud.dto.UserDto;
import com.jose.curso.springboot.app.springboot_crud.entities.Role;
import com.jose.curso.springboot.app.springboot_crud.entities.User;
import com.jose.curso.springboot.app.springboot_crud.mappers.UserMapper;
import com.jose.curso.springboot.app.springboot_crud.repositories.RoleRepository;
import com.jose.curso.springboot.app.springboot_crud.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    // public List<User> findAll() {
    //     return (List<User>) repository.findAll();
    // }
    public List<UserDto> findAll() {
        List<User> users = (List<User>) repository.findAll();
        return users.stream().map(userMapper::userToUserDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findByUsername(String username) {
        User user = repository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("El usuario no ha sido encontrado: " + username));
        return userMapper.userToUserDto(user);
    }

    @Override
    @Transactional
    // public User save(User user) {
    //     Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
    //     List<Role> roles = new ArrayList<>();

    //     optionalRoleUser.ifPresent(roles::add);

    //     if(user.isAdmin()) {
    //         Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
    //         optionalRoleAdmin.ifPresent(roles::add);
    //     }

    //     user.setRoles(roles);
    //     user.setPassword(passwordEncoder.encode(user.getPassword()));
    //     return repository.save(user);
    // }
    public UserDto save(UserDto dto) {
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        User user = new User(dto.getUsername());

        optionalRoleUser.ifPresent(roles::add);

        if(dto.isAdmin()) {
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return userMapper.userToUserDto(repository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username){
        return repository.existsByUsername(username);
    }
}
