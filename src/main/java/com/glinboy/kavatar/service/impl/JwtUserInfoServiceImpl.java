package com.glinboy.kavatar.service.impl;

import com.glinboy.kavatar.service.UserInfoService;
import com.glinboy.kavatar.service.dto.UserInfoDTO;
import org.springframework.stereotype.Service;

@Service
public class JwtUserInfoServiceImpl implements UserInfoService {
	@Override
	public UserInfoDTO getUserInfo() {
		return new UserInfoDTO(
			"00000000000000000000000000000000",
			"John",
			"Doe",
			"John Doe",
			"john",
			"john.doe@email.com"
		);
	}
}
