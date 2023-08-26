package com.glinboy.kavatar.service.impl;

import com.glinboy.kavatar.service.AvatarService;
import com.glinboy.kavatar.service.dto.AvatarDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@ConditionalOnProperty(value = "application.avatars.storage", havingValue = "mongo")
public class MongoAvatarServiceImpl implements AvatarService {
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
		return null;
	}

	@Override
	public Mono<Void> deleteAvatar() {
		return null;
	}

	@Override
	public Mono<AvatarDTO> getDefaultAvatar() {
		return null;
	}
}
