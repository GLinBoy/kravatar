package com.glinboy.kavatar.service.impl;

import com.glinboy.kavatar.service.AvatarService;
import com.glinboy.kavatar.service.UserInfoService;
import com.glinboy.kavatar.service.dto.AvatarDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileAvatarServiceImpl implements AvatarService {

	private final UserInfoService userInfoService;
	@Value("${application.avatars.path}")
	private String pathString;

	@Override
	public Mono<Optional<AvatarDTO>> getAvatar() {
		return getAvatar(userInfoService.getUserInfo().id());
	}

	@Override
	public Mono<Optional<AvatarDTO>> getAvatar(String id) {
		try (Stream<Path> list = Files.list(Paths.get(pathString))) {
			return list.filter(p -> p.toFile().isFile() && p.getFileName().toString().startsWith(id))
				.findAny()
				.map(p -> {
					try {
						return Mono.just(
							Optional.of(
								new AvatarDTO(id, Files.probeContentType(p),
									DataBufferUtils.read(p, new DefaultDataBufferFactory(), getBufferSize()))
							)
						);
					} catch (IOException ex) {
						log.error("Can not detect file type", ex);
					}
					return null;
				})
				.orElseGet(() -> Mono.just(Optional.empty()));
		} catch (IOException ex) {
			log.error("Can not delete avatar file(s)", ex);
		}
		return Mono.just(Optional.empty());
	}

	@Override
	public Mono<String> saveAvatar(Mono<FilePart> filePartMono) {
		Path path = Paths.get(pathString);
		try {
			Files.createDirectories(path);
			return filePartMono
				.flatMap(filePart -> {
					String[] split = filePart.filename().split("\\.");
					String fileExtension = split[split.length - 1];
					String filename = userInfoService.getUserInfo().id();
					return filePart.transferTo(path.resolve(String.format("%s.%s", filename, fileExtension))).then(Mono.just(filename));
				});
		} catch (IOException ex) {
			throw new ResponseStatusException(
				HttpStatus.INTERNAL_SERVER_ERROR,
				"Can't create directory to save files: " + ex.getMessage(),
				ex
			);
		}
	}

	@Override
	public Mono<Void> deleteAvatar() {
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
		return Mono.empty();
	}

	@Override
	public Mono<AvatarDTO> getDefaultAvatar() {
		return null;
	}

	private int getBufferSize() {
		return 64 * 1024;
	}
}

