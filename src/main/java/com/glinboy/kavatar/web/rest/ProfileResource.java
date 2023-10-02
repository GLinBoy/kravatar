package com.glinboy.kavatar.web.rest;

import com.glinboy.kavatar.service.ProfileService;
import com.glinboy.kavatar.service.UserInfoService;
import com.glinboy.kavatar.service.dto.UserInfoDTO;
import com.glinboy.kavatar.util.GeneratorUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileResource {

	private final ProfileService service;
	private final UserInfoService userInfoService;

	@GetMapping
	public ResponseEntity<UserInfoDTO> getProfile() {
		return service.getProfile(userInfoService.getUserInfo().id())
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping(value = {"/{id}", "/{id}.json"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserInfoDTO> getProfileJson(@PathVariable String id) {
		return service.getProfile(id)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping(value = "/{id}.xml", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<UserInfoDTO> getProfileXml(@PathVariable String id) {
		return service.getProfile(id)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping(value = "/{id}.vcf", produces = "text/x-vcard")
	public ResponseEntity<byte[]> getProfileVcf(@PathVariable String id) {
		return service.getProfile(id)
			.map(GeneratorUtils::vCardGenerate)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping(value = "/{id}.qr", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> getProfileQr(@PathVariable String id, HttpServletRequest request) {
		return GeneratorUtils
			.qrGenerate("%s/%s".formatted(ServletUriComponentsBuilder
				.fromRequestUri(request)
				.replacePath(null)
				.build()
				.toUriString(), id))
			.map(v ->
				ResponseEntity
					.ok()
					.contentType(MediaType.IMAGE_PNG)
					.body(v)
			)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}
}
