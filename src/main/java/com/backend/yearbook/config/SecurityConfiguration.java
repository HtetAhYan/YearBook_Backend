package com.backend.yearbook.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static com.backend.yearbook.entity.Role.ADMIN;
import static com.backend.yearbook.user.Permissions.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfiguration {
    @Autowired
    private JwtEntryPoint authEntryPoint;
    private static final String[] WHITE_LIST_URL = {
        "/api/**",
        "/v2/api-docs",
        "/v3/api-docs",
        "/v3/api-docs/**",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui/**",
        "/webjars/**",
        "/swagger-ui.html"};
    private final JWTAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authProvider;
@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

    httpSecurity
        .csrf(AbstractHttpConfigurer::disable)

        .authorizeHttpRequests(req ->
            req.requestMatchers(WHITE_LIST_URL)
                .permitAll()
                //.requestMatchers("/api/v1/admin/**").hasAnyRole(ADMIN.name())
                //.requestMatchers(HttpMethod.GET,"/api/v1/admin/**").hasAnyAuthority(ADMIN_READ.name())
                //.requestMatchers(HttpMethod.POST,"/api/v1/admin/**").hasAnyAuthority(ADMIN_CREATE.name()).
                //requestMatchers(HttpMethod.PUT,"/api/v1/admin/**").hasAnyAuthority(ADMIN_UPDATE.name())
                //.requestMatchers(HttpMethod.DELETE,"/api/v1/admin/**").hasAnyAuthority(ADMIN_DELETE.name())

                .anyRequest().authenticated()
        )
        //.exceptionHandling( ex -> ex.authenticationEntryPoint(authEntryPoint))
      //  .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        .authenticationProvider(authProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


    return httpSecurity.build();
}
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000/");
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
