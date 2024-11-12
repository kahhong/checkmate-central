package com.ila.checkmatecentral.config;

import com.ila.checkmatecentral.service.AccountCredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.servlet.function.RequestPredicates.path;
import static org.springframework.web.servlet.function.RequestPredicates.pathExtension;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(JwtAuthenticationFilter jwtAuthenticationFilter, HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/match/*").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/match/list/*").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/match/*/update").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET, "/api/tournaments/list").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/tournaments/").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/tournaments/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/tournaments/*").hasRole("ADMIN")

                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
            .authenticationManager(authenticationManager)
            .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.ignoringRequestMatchers("/**"))
            .headers(header -> header.disable())
            .cors(cors -> cors.disable());
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://127.0.0.1:8080/"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    RouterFunction<ServerResponse> spaRouter() {
        final List<String> extensions = Arrays.asList("js", "css", "ico", "png", "jpg", "gif", "svg");
        RequestPredicate spaPredicate = path("/api/**")
                .or(path("/error"))
                .or(pathExtension(extensions::contains))
                .negate();
        ClassPathResource index = new ClassPathResource("static/index.html");
        return route().resource(spaPredicate, index).build();
    }

    @Bean
    public AuthenticationManager userAuthenticationManager(AccountCredentialService accountCredentialService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(accountCredentialService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
