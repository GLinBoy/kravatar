package com.glinboy.kavatar.service;

import com.glinboy.kavatar.service.dto.UserInfoDTO;

import java.util.Optional;

public interface ProfileService {
    Optional<UserInfoDTO> getProfile();

	Optional<UserInfoDTO> getProfile(String id);
}
