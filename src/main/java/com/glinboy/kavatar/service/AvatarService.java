package com.glinboy.kavatar.service;

import com.glinboy.kavatar.service.dto.AvatarDTO;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface AvatarService {
	Mono<Optional<AvatarDTO>> getAvatar();
	Mono<Optional<AvatarDTO>> getAvatar(String id);
	Mono<String> saveAvatar(Mono<FilePart> filePartMono);

	Mono<Void> deleteAvatar();

	Mono<AvatarDTO> getDefaultAvatar();
}
