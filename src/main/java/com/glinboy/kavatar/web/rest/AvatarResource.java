package com.glinboy.kavatar.web.rest;

import com.glinboy.kavatar.service.AvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/avatars")
public class AvatarResource {

	private final AvatarService service;

	@GetMapping
	public Mono<ResponseEntity<Flux<DataBuffer>>> getAvatar() {
		return service.getAvatar()
			.map(avatarDTO ->
				avatarDTO.map(a -> ResponseEntity.ok().contentType(MediaType.valueOf(a.fileType())).body(a.fileContent()))
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
			);
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Flux<DataBuffer>>> getAvatar(@PathVariable String id) {
		return service.getAvatar(id)
			.map(avatarDTO ->
				avatarDTO.map(a -> ResponseEntity.ok().contentType(MediaType.valueOf(a.fileType())).body(a.fileContent()))
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
			);
	}

	@PostMapping
	public Mono<ResponseEntity<Void>> uploadImage(@RequestPart("file") Mono<FilePart> file, ServerWebExchange exchange) {
		return service.saveAvatar(file).flatMap(filename -> {
			String imageUrl = exchange.getRequest().getURI() + "/" + filename;
			return Mono.just(ResponseEntity.created(URI.create(imageUrl)).build());
		});
	}

	@DeleteMapping
	public Mono<ResponseEntity<Void>> deleteAvatar() {
		return service
			.deleteAvatar()
			.then(Mono.just(ResponseEntity.noContent().build()));
	}
}
