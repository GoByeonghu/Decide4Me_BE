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
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommentsResponse {
    List<CommentResponse> commentResponses;

    public CommentsResponse(List<Comment> comments){
        this.commentResponses = new ArrayList<CommentResponse>();
        for (Comment comment : comments) {
            CommentResponse commentResponse = new CommentResponse(comment);
            this.commentResponses.add(commentResponse);
        }
    }
}
