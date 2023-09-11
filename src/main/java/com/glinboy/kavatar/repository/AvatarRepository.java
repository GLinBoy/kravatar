package com.glinboy.kavatar.repository;

import com.glinboy.kavatar.entity.Avatar;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AvatarRepository extends MongoRepository<Avatar, String> {
}
