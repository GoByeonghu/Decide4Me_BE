package com.sfz.rest_api.dto;

import com.sfz.rest_api.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse {
    private String nickname;
    private String email;
    private String profileImage;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//    private LocalDateTime lastLogin;

    public UserResponse(User user){
        this.setNickname(user.getNickname());
        this.setEmail(user.getEmail());
        this.setProfileImage(user.getProfileImage());
    }
}
