package com.jose.curso.springboot.app.springboot_crud.security.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jose.curso.springboot.app.springboot_crud.dto.UserDto;
import com.jose.curso.springboot.app.springboot_crud.security.JwtService;
import com.jose.curso.springboot.app.springboot_crud.services.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.jose.curso.springboot.app.springboot_crud.security.JwtService.*;

@Component
public class JwtRefreshTokenValidationFilter extends BasicAuthenticationFilter {

    private JwtService jwtService;

    private UserService userService;

    public JwtRefreshTokenValidationFilter(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
        super(authenticationManager);
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if(!"/refresh".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();

        String token = jwtService.getRefreshToken(cookies);

        if(token == null) {
            Map<String, String> body = new HashMap<>();
            body.put("message", "No hay refresh token");

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(CONTENT_TYPE);

            return;
        }

        try {
            Claims claims = jwtService.verifyRefreshToken(token);
            String username = claims.getSubject();
            UserDto user = userService.findByUsername(username);
            List<String> roles = user.getRoles();
            String accessToken = jwtService.createAccessToken(username, roles);
            String refreshToken = jwtService.createRefreshToken(username);

            ResponseCookie accessJwtCookie = jwtService.createAccessCookie(accessToken);
            ResponseCookie refreshJwtCookie = jwtService.createRefreshCookie(refreshToken);
        
            response.addHeader("Set-cookie", accessJwtCookie.toString());
            response.addHeader("Set-cookie", refreshJwtCookie.toString());

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(CONTENT_TYPE);

            Map<String, String> body = new HashMap<>();
            body.put("message", "Se ha renovado el access token con éxito");
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        } catch (JwtException e) {
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "El token de refresh es inválido");

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(CONTENT_TYPE);
        }

    }

    
}
