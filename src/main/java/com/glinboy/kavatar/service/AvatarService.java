package com.glinboy.kavatar.service;

import com.glinboy.kavatar.service.dto.AvatarDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface AvatarService {
	Optional<AvatarDTO> getAvatar(String id);

	String saveAvatar(MultipartFile multipartFile);

	void deleteAvatar();

	Optional<AvatarDTO> getDefaultAvatar();
}
