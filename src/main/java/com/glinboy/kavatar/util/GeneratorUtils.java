package com.glinboy.kavatar.util;

import com.glinboy.kavatar.service.dto.UserInfoDTO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.StructuredName;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public final class GeneratorUtils {
	private GeneratorUtils() {
	}

	public static Optional<byte[]> vCardGenerate(UserInfoDTO userInfoDTO) {
		return Optional.ofNullable(userInfoDTO).map(p -> {
				VCard vCard = new VCard();
				StructuredName n = new StructuredName();
				n.setFamily(p.family());
				n.setGiven(p.name());
				vCard.setStructuredName(n);
				return Optional.of(Ezvcard
							.write(vCard)
							.go()
							.getBytes(StandardCharsets.UTF_8)
					);
			})
			.orElseGet(Optional::empty);
	}

	public static Optional<byte[]> qrGenerate(String url) {
		try {
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 250, 250);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
			return Optional.of(outputStream.toByteArray());
		} catch (IOException | WriterException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't generate QR", ex);
		}
	}
}
