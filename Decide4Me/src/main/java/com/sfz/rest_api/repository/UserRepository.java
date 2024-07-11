package com.sfz.rest_api.repository;

import com.sfz.rest_api.model.Post;
import com.sfz.rest_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByNickname(String nickname);

    User findByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.nickname = :nickname")
    boolean existsByNickname(@Param("nickname") String nickname);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

}