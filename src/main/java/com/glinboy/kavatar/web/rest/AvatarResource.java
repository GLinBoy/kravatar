package com.glinboy.kavatar.web.rest;

import com.glinboy.kavatar.service.AvatarService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/avatars")
public class AvatarResource {

	private final AvatarService service;

	@GetMapping
	public ResponseEntity<byte[]> getAvatar() {
		return service.getAvatar()
			.map(avatarDTO ->
				ResponseEntity.ok().contentType(MediaType.valueOf(avatarDTO.fileType())).body(avatarDTO.fileContent())
			).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping("/{id}")
	public ResponseEntity<byte[]> getAvatar(@PathVariable String id) {
		return service.getAvatar(id)
			.map(avatarDTO ->
				ResponseEntity.ok().contentType(MediaType.valueOf(avatarDTO.fileType())).body(avatarDTO.fileContent())
			).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@PostMapping
	public ResponseEntity<Void> uploadImage(@RequestPart("file") MultipartFile file, HttpServletRequest request) {
		String idName = service.saveAvatar(file);
		String imageUrl = request.getRequestURL().toString() + "/" + idName;
		return ResponseEntity.created(URI.create(imageUrl)).build();
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteAvatar() {
		service.deleteAvatar();
		return ResponseEntity.noContent().build();
	}
}
