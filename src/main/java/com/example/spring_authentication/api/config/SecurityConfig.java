package com.example.spring_authentication.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private SecurityFilterConfig securityFilterConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "usuario/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "usuario/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "usuario/list/valid/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "usuario/list/invalid/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "usuario/list/admin/invalid/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "usuario/detail/**").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.GET, "usuario/validate/account/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "usuario/update/user/**").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "usuario/suspense/account/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "usuario/reactivate/account/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "usuario/update/role/**").hasAnyRole("ADMIN"))
                .addFilterBefore(securityFilterConfig, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
