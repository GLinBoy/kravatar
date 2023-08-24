package com.glinboy.kavatar.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
	name = OpenApiConfig.SECURITY_SCHEME_NAME,
	type = SecuritySchemeType.HTTP,
	bearerFormat = "JWT",
	scheme = "bearer"
)
public class OpenApiConfig {
	public static final String SECURITY_SCHEME_NAME = "Bearer Authentication";
}
