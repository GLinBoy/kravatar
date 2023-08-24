package com.glinboy.kavatar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
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
	SecurityWebFilterChain filterChain(ServerHttpSecurity security) {
		return security
			.cors(Customizer.withDefaults())
			.csrf(Customizer.withDefaults())
			.authorizeExchange(exchanges ->
				exchanges
					.pathMatchers(authWhitelist).permitAll()
					.anyExchange().authenticated()
			)
			.oauth2ResourceServer(it -> it.jwt(Customizer.withDefaults()))
			.build();
	}
}
