package com.glinboy.kavatar.web.rest;

import com.glinboy.kavatar.service.ProfileService;
import com.glinboy.kavatar.service.dto.UserInfoDTO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.StructuredName;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileResource {
	private final ProfileService service;

	@GetMapping
	public ResponseEntity<UserInfoDTO> getProfile() {
		return service.getProfile()
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

}
