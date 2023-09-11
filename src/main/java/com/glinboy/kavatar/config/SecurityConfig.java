package com.glinboy.kavatar.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	private final String[] authWhitelist = {
		"/",
		"/**",
		"/swagger-ui.html",
		"/v3/api-docs/**",
		"/swagger-ui/**",
		// other public endpoints of your API may be appended to this array
		"/h2-console/**"
	};

	@Bean
	@SneakyThrows
	SecurityFilterChain filterChain(HttpSecurity http) {
		return http
			.cors(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
			.authorizeHttpRequests(request ->
				request
					.requestMatchers(authWhitelist).permitAll()
					.anyRequest().authenticated()
			)
			.oauth2ResourceServer(it -> it.jwt(Customizer.withDefaults()))
			.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.build();
	}
}
