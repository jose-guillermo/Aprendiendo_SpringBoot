package com.jose.curso.springboot.app.springboot_crud.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// Esta clase ya no es necesaria ya que paso las authorities como una lista de String
public abstract class SimpleGrantedAuthorityJsonCreator {
    
    @JsonCreator
    public SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role){};
    
}
