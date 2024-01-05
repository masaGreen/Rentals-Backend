package com.masagreen.RentalUnitsManagement.security;

import com.masagreen.RentalUnitsManagement.security.jwt.JwtAuthenticationEntryPoint;
import com.masagreen.RentalUnitsManagement.security.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class WebConfig {
    private final String[] allowedList = {
            "/v1/auth/login",
            "/v1/auth/signup",
            "/swagger-ui.html",
            "swagger-ui/**",
            "/v3/api-docs/**",
            "/v1/auth/validate-email/**"
    };
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    public HandlerExceptionResolver handlerExceptionResolver;


    @Autowired
    private AuthenticationProvider authenticationProvider;


    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(handlerExceptionResolver);
    }

    @Bean
    public SecurityFilterChain securityFilterChain1(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {

                    auth.requestMatchers(allowedList).permitAll();
                    auth.anyRequest().authenticated();

                })
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint));


        return http.build();

    }


}
