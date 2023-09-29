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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

	@GetMapping(value = "/{id}.vcf", produces = "text/x-vcard")
	public ResponseEntity<InputStreamResource> getProfileVcf(@PathVariable String id) {
		return service.getProfile(id)
			.map(p -> {
				VCard vCard = new VCard();
				StructuredName n = new StructuredName();
				n.setFamily(p.family());
				n.setGiven(p.name());
				vCard.setStructuredName(n);
				return new InputStreamResource(
					new ByteArrayInputStream(
						Ezvcard
							.write(vCard)
							.go()
							.getBytes(StandardCharsets.UTF_8)
					)
				);
			})
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping(value = "/{id}.qr", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> getProfileQr(@PathVariable String id, HttpServletRequest request) {
		// FIXME Generate QR to redirect to user profile page
		try {
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(request.getRequestURL().toString().replace(".qr", ""), BarcodeFormat.QR_CODE, 250, 250);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
			return ResponseEntity
				.ok()
				.contentType(MediaType.IMAGE_PNG)
				.body(outputStream.toByteArray());
		} catch (IOException | WriterException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't generate QR", ex);
		}
	}
}
