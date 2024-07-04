package com.sfz.rest_api.repository;

import com.sfz.rest_api.model.Post;
import com.sfz.rest_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByNickname(String nickname);
}