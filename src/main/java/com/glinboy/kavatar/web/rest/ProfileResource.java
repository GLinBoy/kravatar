package com.glinboy.kavatar.web.rest;

import com.glinboy.kavatar.service.ProfileService;
import com.glinboy.kavatar.service.dto.UserInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileResource {
	private final ProfileService service;

	@GetMapping
	public ResponseEntity<UserInfoDTO> getProfile() {
		return service.getProfile()
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserInfoDTO> getProfile(@PathVariable String id) {
		return service.getProfile(id)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}
}
