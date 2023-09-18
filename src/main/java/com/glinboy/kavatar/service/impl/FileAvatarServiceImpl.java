package com.glinboy.kavatar.service.impl;

import com.glinboy.kavatar.service.AvatarService;
import com.glinboy.kavatar.service.UserInfoService;
import com.glinboy.kavatar.service.dto.AvatarDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
	value = "application.avatars.storage",
	havingValue = "file",
	matchIfMissing = true
)
public class FileAvatarServiceImpl implements AvatarService {

	private final UserInfoService userInfoService;
	@Value("${application.avatars.path}")
	private String pathString;

	@Override
	@Cacheable(value = "avatar", key = "#result.userId()", unless = "#result != null")
	public Optional<AvatarDTO> getAvatar() {
		return getAvatar(userInfoService.getUserInfo().id());
	}

	@Override
	public Optional<AvatarDTO> getAvatar(String id) {
		try (Stream<Path> list = Files.list(Paths.get(pathString))) {
			return list.filter(p -> p.toFile().isFile() && p.getFileName().toString().startsWith(id))
				.findAny()
				.map(p -> {
					try {
						return Optional.of(new AvatarDTO(id, Files.probeContentType(p), Files.readAllBytes(p)));
					} catch (IOException ex) {
						log.error("Can not detect file type", ex);
					}
					return Optional.<AvatarDTO>empty();
				}).orElseGet(Optional::empty);
		} catch (IOException ex) {
			log.error("Can not delete avatar file(s)", ex);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't get the avatar file", ex);
		}
	}

	@Override
	public String saveAvatar(MultipartFile multipartFile) {
		Path path = Paths.get(pathString);
		try {
			if (!Files.exists(path)) {
				Files.createDirectories(path);
			}
			String[] split = multipartFile.getOriginalFilename().split("\\.");
			String fileExtension = split[split.length - 1];
			String filename = userInfoService.getUserInfo().id();
			multipartFile.transferTo(path.resolve(String.format("%s.%s", filename, fileExtension)));
			return filename;
		} catch (IOException ex) {
			throw new ResponseStatusException(
				HttpStatus.INTERNAL_SERVER_ERROR,
				"Can't save file to directory: " + ex.getMessage(),
				ex
			);
		}
	}

	@Override
	public void deleteAvatar() {
		try (Stream<Path> list = Files.list(Paths.get(pathString))) {
			list.filter(p -> p.toFile().isFile() && p.getFileName().toString().startsWith(userInfoService.getUserInfo().id()))
				.forEach(p -> {
					try {
						Files.delete(p);
					} catch (IOException ex) {
						log.error(String.format("Can not delete avatar file: %s", p), ex);
					}
				});
		} catch (IOException ex) {
			log.error("Can not delete avatar file(s)", ex);
		}
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

