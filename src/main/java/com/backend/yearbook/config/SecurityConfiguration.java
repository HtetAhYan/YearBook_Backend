package com.backend.yearbook.config;

import com.backend.yearbook.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@EnableMethodSecurity
public class SecurityConfiguration {
    @Autowired
    private JwtEntryPoint authEntryPoint;
    private static final String[] WHITE_LIST_URL = {
        "/api/v1/auth/**",
      };
    private final JWTAuthFilter jwtAuthFilter;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

    httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
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
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


    return httpSecurity.build();
}


    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService.userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

}
