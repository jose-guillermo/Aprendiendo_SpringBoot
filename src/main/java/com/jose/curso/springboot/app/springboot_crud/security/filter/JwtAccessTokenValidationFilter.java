package com.jose.curso.springboot.app.springboot_crud.security.filter;

// import static com.jose.curso.springboot.app.springboot_crud.security.TokenJwtConfig.HEADER_AUTHORIZATION;
// import static com.jose.curso.springboot.app.springboot_crud.security.TokenJwtConfig.PREFIX_TOKEN;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jose.curso.springboot.app.springboot_crud.security.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.jose.curso.springboot.app.springboot_crud.security.JwtService.*;

public class JwtAccessTokenValidationFilter extends BasicAuthenticationFilter{

    private JwtService jwtService;

    public JwtAccessTokenValidationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Hacerlo con header authorization
        // String header = request.getHeader(HEADER_AUTHORIZATION);
        // if(header == null || !header.startsWith(PREFIX_TOKEN)){
        //     chain.doFilter(request, response);
        //     return;
        // };
        
        // String token = header.replace(PREFIX_TOKEN, "");
        // String token = header.substring(PREFIX_TOKEN.length()).trim();

        Cookie[] cookies = request.getCookies();
        
        String token = jwtService.getAccessToken(cookies);

        if(token == null) {
            // Map<String, String> body = new HashMap<>();
            // body.put("message", "Token de acceso no encontrado o no proporcionado.");

            // response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            // response.setStatus(HttpStatus.UNAUTHORIZED.value());
            // response.setContentType(CONTENT_TYPE);
            chain.doFilter(request, response);
            return;
        }
        
        try {
            Claims claims = jwtService.verifyAccessToken(token);
            String username = claims.getSubject();
            // String username2 = claims.get("username") + "";

            // establecer los roles con un mixim
            // Object authoritiesClaims = claims.get("authorities");
            // Collection<? extends GrantedAuthority> authorities = Arrays.asList(
            //     new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class).readValue(authoritiesClaims.toString().getBytes(), 
            //     SimpleGrantedAuthority[].class)
            // );
            Object rolesObj = claims.get("authorities");
            List<String> roleNames = new ArrayList<>();

            if(rolesObj instanceof List<?>) {
                for (Object item : (List<?>) rolesObj) {
                    if(item instanceof String) {
                        roleNames.add((String) item);
                    }
                }
            } 

            Collection<? extends GrantedAuthority> authorities = roleNames.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response);
        } catch (JwtException e) {
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "El token de acceso es inválido");

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(CONTENT_TYPE);
        }
    }

}
