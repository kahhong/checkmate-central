package com.ila.checkmatecentral.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/match/*").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/match/list/*").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/match/*/update").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET, "/tournaments/*").authenticated()
                .requestMatchers(HttpMethod.POST, "/tournaments/").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/tournaments/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/tournaments/*").hasRole("ADMIN")

                .requestMatchers("/h2-console/**").permitAll()
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.ignoringRequestMatchers("/**"))
            .headers(header -> header.disable());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
