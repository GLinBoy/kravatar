package com.glinboy.kavatar.service.dto;

import java.awt.image.DataBuffer;

public record AvatarDTO(
	String userId,
	String fileType,
	DataBuffer fileContent
) {
}
