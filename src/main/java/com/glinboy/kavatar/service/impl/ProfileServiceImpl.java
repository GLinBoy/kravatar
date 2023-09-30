package com.glinboy.kavatar.service.impl;

import com.glinboy.kavatar.service.ProfileService;
import com.glinboy.kavatar.service.UserInfoService;
import com.glinboy.kavatar.service.dto.UserInfoDTO;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
	private final Keycloak keycloak;
	private final UserInfoService userInfoService;

	@Value("${application.keycloak.client.realm}")
	private String realm;

	@Override
	@Cacheable(value = "profile", key = "#id", unless = "#result == null")
	public Optional<UserInfoDTO> getProfile(String id) {
		try {
			UserRepresentation representation = keycloak.realm(realm)
				.users()
				.get(id)
				.toRepresentation();
			return Optional.of(new UserInfoDTO(
				representation.getId(),
				representation.getFirstName(),
				representation.getLastName(),
				representation.getUsername(),
				representation.getEmail(),
				representation.isEmailVerified(),
				representation.getCreatedTimestamp(),
				representation.getAttributes(),
				representation.getGroups(),
				representation.getRealmRoles(),
				representation.getClientRoles()
			));
		} catch (NotFoundException ex) {
			throw new ResponseStatusException(ex.getResponse().getStatus(),
				"Can not found user id: %s".formatted(id), ex);
		}
	}
}
