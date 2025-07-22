package com.example.pancharm.configuration;

import com.example.pancharm.constant.PredefineRole;
import lombok.experimental.NonFinal;
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
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@NonFinal
	@Value("${jwt.signerKey}")
	protected String SIGNER_KEY;
	private final String[] PUBLIC_GET_ENDPOINTS = {"/"};
	private final String[] PUBLIC_POST_ENDPOINTS = {
			"/users", "/auth/login", "/auth/introspect", "/auth/register", "/auth/forgot-password"
	};

	@Autowired
	private CustomJwtDecoder customJwtDecoder;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeHttpRequests(request -> request
				.requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()
				.requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()
				.requestMatchers(HttpMethod.GET, "/users").hasAuthority(PredefineRole.SUPER_ADMIN.getName())
				.anyRequest().authenticated());

		httpSecurity.oauth2ResourceServer(oauth2 -> {
			oauth2.jwt(jwtConfigurer -> {
				jwtConfigurer.decoder(customJwtDecoder)
						.jwtAuthenticationConverter(jwtAuthenticationConverter());
			}).authenticationEntryPoint(new JWTAuthenticationEntryPoint());
		});

		httpSecurity.csrf(AbstractHttpConfigurer::disable);

		return httpSecurity.build();
	}


	@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter  authoritiesConverter = new JwtGrantedAuthoritiesConverter();
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
