package com.glinboy.kavatar.service.impl;

import com.glinboy.kavatar.repository.AvatarRepository;
import com.glinboy.kavatar.service.AvatarService;
import com.glinboy.kavatar.service.UserInfoService;
import com.glinboy.kavatar.service.dto.AvatarDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "application.avatars.storage", havingValue = "mongo")
public class MongoAvatarServiceImpl implements AvatarService {

	private final AvatarRepository repository;
	private final UserInfoService userInfoService;

	@Override
	public Optional<AvatarDTO> getAvatar() {
		return null;
	}

	@Override
	public Optional<AvatarDTO> getAvatar(String id) {
		return null;
	}

	@Override
	public String saveAvatar(MultipartFile multipartFile) {
		String userId = userInfoService.getUserInfo().id();

		return userId;
	}

	@Override
	public void deleteAvatar() {
		repository.deleteById(userInfoService.getUserInfo().id());
	}

	@Override
	public Optional<AvatarDTO> getDefaultAvatar() {
		try {
			File file = ResourceUtils.getFile("/static/images/default_avatar.jpg");
			return Optional.of(
				new AvatarDTO(
					userInfoService.getUserInfo().id(),
					Files.probeContentType(file.toPath()),
					Files.readAllBytes(file.toPath())
				)
			);
		} catch (IOException | NullPointerException ex) {
			log.error("Can not load default user avatar file", ex);
		}
		return Optional.empty();
	}
}
