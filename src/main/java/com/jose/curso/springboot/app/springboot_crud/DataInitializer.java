package com.jose.curso.springboot.app.springboot_crud;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.jose.curso.springboot.app.springboot_crud.entities.Role;
import com.jose.curso.springboot.app.springboot_crud.entities.User;
import com.jose.curso.springboot.app.springboot_crud.repositories.RoleRepository;
import com.jose.curso.springboot.app.springboot_crud.repositories.UserRepository;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    DataInitializer(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void createRoles() {
        if(roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role admin = new Role();
            admin.setName("ROLE_ADMIN");
            roleRepository.save(admin);
        }

        if(roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role user = new Role();
            user.setName("ROLE_USER");
            roleRepository.save(user);
        }
    }

    @PostConstruct
    public void createAdmin() {
        createRoles();

        if(userRepository.findByUsername("admin").isEmpty()) {
            User user = new User();
            List<Role> roles = new ArrayList<>();
    
            roleRepository.findByName("ROLE_USER").ifPresent(roles::add);;
            roleRepository.findByName("ROLE_ADMIN").ifPresent(roles::add);;
    
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode("1234"));
            user.setUsername("admin");
            user.setRoles(roles);
    
            userRepository.save(user);
        }
    }
}
