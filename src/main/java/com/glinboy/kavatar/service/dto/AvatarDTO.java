package com.glinboy.kavatar.service.dto;

import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;

public record AvatarDTO(
	String userId,
	String fileType,
	Flux<DataBuffer> fileContent
) {
}
