package com.jose.curso.springboot.app.springboot_crud.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jose.curso.springboot.app.springboot_crud.validation.ExistsByUserName;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDto {

    private Long id;

    @ExistsByUserName
    @NotBlank
    @Size(min = 4, max = 12)
    private String username;

    @NotBlank
    // @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private boolean enabled;
    private List<String> roles;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isAdmin() {
        return admin;
    }
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
