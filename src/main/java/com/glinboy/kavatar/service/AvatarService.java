package com.glinboy.kavatar.service;

import com.glinboy.kavatar.service.dto.AvatarDTO;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface AvatarService {
	Mono<AvatarDTO> getAvatar();
	Mono<AvatarDTO> getAvatar(String id);
	Mono<String> saveAvatar(Mono<FilePart> filePartMono);

	Mono<Void> deleteAvatar();

	Mono<AvatarDTO> getDefaultAvatar();
}
