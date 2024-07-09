package com.sfz.rest_api.dto;

import com.sfz.rest_api.model.Post;
import com.sfz.rest_api.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentRequest {
    private Long id;
    private Long post_id;
    private String user_id;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
