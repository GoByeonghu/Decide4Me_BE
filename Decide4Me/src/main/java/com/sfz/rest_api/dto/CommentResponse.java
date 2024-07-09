package com.sfz.rest_api.dto;

import com.sfz.rest_api.model.Comment;
import com.sfz.rest_api.model.Post;
import com.sfz.rest_api.model.User;
import com.sfz.rest_api.model.VoteOption;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CommentResponse {
    private Long id;
    private Long post_id;
    private String user_id;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public CommentResponse(Comment comment){
        this.setId(comment.getId());
        this.setPost_id(comment.getPost().getId());
        this.setUser_id(comment.getUser().getId());
        this.setContent(comment.getContent());
        this.setCreated_at(comment.getCreatedAt());
        this.setUpdated_at(comment.getUpdatedAt());
    }
}
