package com.glinboy.kavatar.repository;

import com.glinboy.kavatar.entity.Avatar;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AvatarRepository extends ReactiveMongoRepository<Avatar, String> {
}
