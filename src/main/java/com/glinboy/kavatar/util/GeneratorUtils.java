package com.glinboy.kavatar.util;

import com.glinboy.kavatar.service.dto.UserInfoDTO;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.StructuredName;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public final class GeneratorUtils {
	private GeneratorUtils() {
	}

	public static Optional<InputStreamResource> vCardGenerate(UserInfoDTO userInfoDTO) {
		return Optional.ofNullable(userInfoDTO).map(p -> {
				VCard vCard = new VCard();
				StructuredName n = new StructuredName();
				n.setFamily(p.family());
				n.setGiven(p.name());
				vCard.setStructuredName(n);
				return Optional.of(new InputStreamResource(
					new ByteArrayInputStream(
						Ezvcard
							.write(vCard)
							.go()
							.getBytes(StandardCharsets.UTF_8)
					)
				));
			})
			.orElseGet(Optional::empty);
	}
}
