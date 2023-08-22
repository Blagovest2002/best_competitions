package com.example;

import com.example.jwt_configuration.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
@ComponentScan
public class SecurityConfiguration {
    private final JwtAuthenticationFilter authFilter ;
    private final AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers("/event/add").hasAuthority("organizer")//to do specify 401 exception message
                        .requestMatchers("/users/events").hasAuthority("organizer")
                        .requestMatchers("/event/{id}/weight_classes/add").hasAuthority("organizer")
                        .requestMatchers("users/event/{id}/register").hasAuthority("competitor")
                        .requestMatchers("/users/profile").authenticated()
                        .requestMatchers("/users/info/{id}").authenticated()
                        .requestMatchers("/users/logout").authenticated()
                        .requestMatchers("/event/{event_id}/weight_class/{weight_class_id}/participants").permitAll()
                        .requestMatchers("/users").permitAll()
                        .requestMatchers("/users/login").permitAll()
                        .requestMatchers("/event/{id}").permitAll()
                        .requestMatchers("/event/{id}/weight_classes").permitAll()
                        .requestMatchers("/event/upcoming").permitAll()
                        .requestMatchers("event/upcoming/country").permitAll()
                        .requestMatchers("/countries/show").permitAll()
                        .anyRequest().denyAll())
                .sessionManagement(customizer-> customizer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(customizer -> customizer
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .build();
    }
}
