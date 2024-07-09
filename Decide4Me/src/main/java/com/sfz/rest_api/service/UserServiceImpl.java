package com.sfz.rest_api.service;

import com.sfz.rest_api.model.Comment;
import com.sfz.rest_api.model.Post;
import com.sfz.rest_api.model.User;
import com.sfz.rest_api.repository.UserRepository;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname).orElse(null);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String id, User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            // Update nickname
            if (user.getNickname() != null) {
                existingUser.setNickname(user.getNickname());
            }

            // Update password
            if (user.getPassword() != null) {
                existingUser.setPassword(user.getPassword());
            }

            // Update email
            if (user.getEmail() != null) {
                existingUser.setEmail(user.getEmail());
            }

            // Update createdAt
            if (user.getCreatedAt() != null) {
                existingUser.setCreatedAt(user.getCreatedAt());
            }

            // Update updatedAt
            if (user.getUpdatedAt() != null) {
                existingUser.setUpdatedAt(user.getUpdatedAt());
            }

            // Update lastLogin
            if (user.getLastLogin() != null) {
                existingUser.setLastLogin(user.getLastLogin());
            }

            // Update profileImage
            if (user.getProfileImage() != null) {
                existingUser.setProfileImage(user.getProfileImage());
            }
            return userRepository.save(existingUser);
        }
        return null;
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}