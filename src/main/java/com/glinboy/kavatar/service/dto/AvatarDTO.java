package com.glinboy.kavatar.service.dto;

public record AvatarDTO(
	String userId,
	String fileType,
	byte[] fileContent
) {
}
