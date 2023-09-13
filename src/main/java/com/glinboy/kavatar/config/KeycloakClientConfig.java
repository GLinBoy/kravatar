package com.glinboy.kavatar.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakClientConfig {

	@Value("${application.keycloak.client.server-url}")
	private String serverUrl;

	@Value("${application.keycloak.client.realm}")
	private String realm;

	@Value("${application.keycloak.client.client-id}")
	private String clientId;

	@Value("${application.keycloak.client.client-secret}")
	private String clientSecret;

	@Bean
	public Keycloak getKeycloak() {
		return KeycloakBuilder.builder()
			.serverUrl(serverUrl)
			.realm(realm)
			.grantType(OAuth2Constants.CLIENT_CREDENTIALS)
			.clientId(clientId)
			.clientSecret(clientSecret)
			.build();
	}
}
