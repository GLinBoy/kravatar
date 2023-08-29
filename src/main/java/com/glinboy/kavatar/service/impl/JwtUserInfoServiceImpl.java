package com.glinboy.kavatar.service.impl;

import com.glinboy.kavatar.service.UserInfoService;
import com.glinboy.kavatar.service.dto.UserInfoDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class JwtUserInfoServiceImpl implements UserInfoService {
	@Override
	public UserInfoDTO getUserInfo() {
		JwtAuthenticationToken authenticationToken =
			(JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		Jwt jwt = (Jwt) authenticationToken.getCredentials();
		return new UserInfoDTO(
			(String) jwt.getClaims().get("sub"),
			(String) jwt.getClaims().get("given_name"),
			(String) jwt.getClaims().get("family_name"),
			(String) jwt.getClaims().get("name"),
			(String) jwt.getClaims().get("preferred_username"),
			(String) jwt.getClaims().get("email")
		);
	}
}
