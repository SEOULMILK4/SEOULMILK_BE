package com.seoulmilk.seoulmilkServer.global.config;

import com.seoulmilk.seoulmilkServer.global.jwt.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;  // JWT 필터 추가

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
//            .sessionManagement(
//                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/home", "swagger-ui/**", "/v3/api-docs/**")
                .permitAll()
                .requestMatchers("/api/employee/login", "/api/employee/refresh/**")
                .permitAll()
                .requestMatchers("/api/employee/find-password/**", "/api/employee/update-password")
                .permitAll()
                .requestMatchers("/api/admin/login", "/api/admin/refresh/**",
                    "/api/admin/agency/**", "/api/admin/employee/**")
                .permitAll()
                .requestMatchers("/api/agency/register/**", "/api/agency/login",
                    "/api/agency/refresh/**",
                    "/api/agency/find-password/**", "/api/agency/update-password")
                .permitAll()
                .anyRequest().authenticated())
            .formLogin(AbstractAuthenticationFilterConfigurer::disable)
            .logout(LogoutConfigurer::permitAll);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
