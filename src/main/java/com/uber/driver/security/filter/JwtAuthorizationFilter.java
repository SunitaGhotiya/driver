package com.uber.driver.security.filter;

import com.uber.driver.model.PhoneNumberAuthenticationToken;
import com.uber.driver.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter{

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String phoneNumber = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            phoneNumber = jwtService.extractPhoneNumber(token);
        }

        if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtService.validateToken(token, phoneNumber)) {
                PhoneNumberAuthenticationToken authToken = new PhoneNumberAuthenticationToken(phoneNumber, Collections.singleton(new SimpleGrantedAuthority("ROLE_DRIVER")));
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                authToken.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
