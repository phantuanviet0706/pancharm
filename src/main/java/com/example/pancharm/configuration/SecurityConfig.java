package com.example.pancharm.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.example.pancharm.constant.PredefineRole;

import lombok.experimental.NonFinal;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    private static final String[] PUBLIC_GET_ENDPOINTS = {
        "/", "/company", "/company/info", "/roles", "/permissions", "/categories",
    };

    private static final String[] PUBLIC_POST_ENDPOINTS = {
        "/users",
        "/auth/login",
        "/auth/introspect",
        "/auth/register",
        "/auth/forgot-password",
        "/auth/logout",
        "/auth/refresh",
        "/roles",
        "/categories",
        "/permissions"
    };

    private static final String[] PUBLIC_PUT_ENDPOINTS = {"/roles/{id}", "/categories/{id}", "/permissions/{id}"};

    private static final String[] PUBLIC_DELETE_ENDPOINTS = {"/roles/{id}", "/categories/{id}", "/permissions/{id}"};

    @Autowired
    private CustomJwtDecoder customJwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS)
                .permitAll()
                .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS)
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/users")
                .hasAuthority(PredefineRole.SUPER_ADMIN.getName())
                .requestMatchers(HttpMethod.PUT, PUBLIC_PUT_ENDPOINTS)
                .permitAll()
                .requestMatchers(HttpMethod.DELETE, PUBLIC_DELETE_ENDPOINTS)
                .permitAll()
                .anyRequest()
                .authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 -> {
            oauth2.jwt(jwtConfigurer -> {
                        jwtConfigurer
                                .decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter());
                    })
                    .authenticationEntryPoint(new JWTAuthenticationEntryPoint());
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(cors -> {});

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return converter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
