package com.glinboy.kavatar.service.impl;

import com.glinboy.kavatar.entity.Avatar;
import com.glinboy.kavatar.repository.AvatarRepository;
import com.glinboy.kavatar.service.AvatarService;
import com.glinboy.kavatar.service.UserInfoService;
import com.glinboy.kavatar.service.dto.AvatarDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Binary;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
	public Mono<Optional<AvatarDTO>> getAvatar() {
		return null;
	}

	@Override
	public Mono<Optional<AvatarDTO>> getAvatar(String id) {
		return null;
	}

	@Override
	public Mono<String> saveAvatar(Mono<FilePart> filePartMono) {
		String userId = userInfoService.getUserInfo().id();
		return filePartMono
			.flatMap(filePart -> {
				filePart.content().flatMap(dataBuffer -> {
						byte[] bytes = new byte[dataBuffer.readableByteCount()];
						dataBuffer.read(bytes);
						return Mono.just(bytes);
					})
					.last()
					.map(imageBytes ->
						repository.save(
							Avatar.builder()
								.userId(userId)
								.fileType(
									Optional.ofNullable(filePart.headers().getContentType())
										.orElseGet(() -> MediaType.IMAGE_JPEG)
										.toString()
								)
								.fileContent(new Binary(imageBytes))
								.build()
						));
				return Mono.just(userId);
			});
	}

	@Override
	public Mono<Void> deleteAvatar() {
		return repository.deleteById(userInfoService.getUserInfo().id());
	}

	@Override
	public Mono<AvatarDTO> getDefaultAvatar() {
		try {
			File file = new File(this.getClass().getResource("/static/images/default_avatar.jpg").getFile());
			return Mono.just(new AvatarDTO(userInfoService.getUserInfo().id(), Files.probeContentType(file.toPath()),
				DataBufferUtils.read(file.toPath(), new DefaultDataBufferFactory(), 64 * 1024))
			);
		} catch (IOException | NullPointerException ex) {
			log.error("Can not load default user avatar file", ex);
		}
		return Mono.empty();
	}
}
