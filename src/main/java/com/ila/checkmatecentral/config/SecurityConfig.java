package com.ila.checkmatecentral.config;

import static org.springframework.web.servlet.function.RequestPredicates.path;
import static org.springframework.web.servlet.function.RequestPredicates.pathExtension;
import static org.springframework.web.servlet.function.RouterFunctions.route;

import java.util.Arrays;
import java.util.List;

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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import com.ila.checkmatecentral.repository.UserAccountRepository;
import com.ila.checkmatecentral.service.UserAccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserAccountRepository repository;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

//                .requestMatchers(HttpMethod.GET, "/match/*").hasAnyRole("USER", "ADMIN")
//                .requestMatchers(HttpMethod.GET, "/match/list/*").hasAnyRole("USER", "ADMIN")
//                .requestMatchers(HttpMethod.PUT, "/match/*/update").hasRole("ADMIN")
//
//                .requestMatchers(HttpMethod.GET, "/tournaments/list").permitAll()
//                .requestMatchers(HttpMethod.POST, "/tournaments/").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.PUT, "/tournaments/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/tournaments/*").hasRole("ADMIN")

                .requestMatchers("/h2-console/**").permitAll()
//                .anyRequest().authenticated()
                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .authenticationManager(authenticationManager)
            .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.ignoringRequestMatchers("/**"))
            .headers(header -> header.disable())
            .cors(cors -> corsConfigurationSource());
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000/"));
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
    public AuthenticationManager authenticationManager(UserAccountService userDetailsService,
                                                       PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
