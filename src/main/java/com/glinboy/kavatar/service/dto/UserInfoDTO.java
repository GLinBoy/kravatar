package com.glinboy.kavatar.service.dto;

import java.util.List;
import java.util.Map;

public record UserInfoDTO(
	String id,
	String name,
	String family,
	String username,
	String email,
	Boolean emailVerified,
	Long createdAt,
	Map<String, List<String>> attributes,
	List<String> groups,
	List<String> realmRoles,
	Map<String, List<String>> clientRoles
	) {
}
