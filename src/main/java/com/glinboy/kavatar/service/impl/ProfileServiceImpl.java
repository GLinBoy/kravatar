package com.glinboy.kavatar.service.impl;

import com.glinboy.kavatar.service.ProfileService;
import com.glinboy.kavatar.service.UserInfoService;
import com.glinboy.kavatar.service.dto.UserInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
	private final Keycloak keycloak;
	private final UserInfoService userInfoService;

	@Override
	public Optional<UserInfoDTO> getProfile() {
		UserRepresentation representation = keycloak.realm("oauth-realm")
			.users()
			.get(userInfoService.getUserInfo().id())
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
	}
}
