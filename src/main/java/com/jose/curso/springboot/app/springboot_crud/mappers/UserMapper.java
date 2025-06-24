package com.jose.curso.springboot.app.springboot_crud.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.jose.curso.springboot.app.springboot_crud.dto.UserDto;
import com.jose.curso.springboot.app.springboot_crud.entities.Role;
import com.jose.curso.springboot.app.springboot_crud.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "admin", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserDto userToUserDto(User user);
    
    User userDtoToUser(UserDto user);

    default String mapRoleToString(Role role) {
        return role.getName(); // Suponiendo que Role tiene un método getName()
    }

    default Role mapStringToRole(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return role;
    }
}
