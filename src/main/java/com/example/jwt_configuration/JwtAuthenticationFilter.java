package com.example.jwt_configuration;

import com.example.controller.ExceptionController;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Data
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String HEADER_START = "Bearer ";
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final HandlerExceptionResolver handlerExceptionResolver;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException  {
      try {
           if (request.getServletPath().contains("/users/login")) {
               filterChain.doFilter(request, response);
               return;
           }
           final String authHeader = request.getHeader("Authorization");
           final String jwt;
           final String userEmail;
           if (authHeader == null || !authHeader.startsWith(HEADER_START)) {
               filterChain.doFilter(request, response);
               return;
           }
           jwt = authHeader.substring(HEADER_START.length());
           userEmail = jwtService.extractEmail(jwt);
           if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
               UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
               if (jwtService.isTokenValid(jwt, userDetails)) {
                   UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                           userDetails,
                           null,
                           userDetails.getAuthorities());
                   authToken.setDetails(new WebAuthenticationDetailsSource()
                           .buildDetails(request));
                   SecurityContextHolder.getContext().setAuthentication(authToken);
               } else {
                   jwtService.throwExc("This token is expired");
               }
           }
           filterChain.doFilter(request, response);
       } catch (ExpiredJwtException e){
           handlerExceptionResolver.resolveException(request,response,null,e);
      }
    }
}
