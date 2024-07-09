package com.sfz.rest_api.dto;
import com.sfz.rest_api.model.Post;
import com.sfz.rest_api.model.Comment;
import com.sfz.rest_api.model.User;
import com.sfz.rest_api.model.VoteOption;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class PostResponse {

    private Long id;
    private String user_id;//private User user; //닉네임만
    private String title;
    private String image;
    private String content;
    private LocalDateTime created_at;//createdAt; //이름 변경
    private LocalDateTime updated_at;//updatedAt; //이름 변경
    private LocalDateTime deadline;
//    private List<VoteOption> voteOptions;
//    private List<Comment> comments;
    private List<VoteOptionResponse> voteOptions;
    private List<CommentResponse> comments;
//    private VoteOptionsResponse voteOptions;
//    private CommentsResponse comments;

    public PostResponse(Post post, String staticURL, List<VoteOption> voteOptions, List<Comment> comments){
        this.setId(post.getId());
        this.setUser_id(post.getUser().getNickname());
        this.setTitle(post.getTitle());
        if(post.getImage() != null){
            this.setImage(staticURL + post.getImage());
        }
        this.setContent(post.getContent());
        this.setCreated_at(post.getCreatedAt());
        this.setUpdated_at(post.getUpdatedAt());
        this.setDeadline(post.getDeadline());

        // VoteOption 리스트를 index 기준으로 오름차순 정렬
        Collections.sort(voteOptions, Comparator.comparingInt(VoteOption::getIndex));
        // Comment 리스트를 createdAt 기준으로 내림차순 정렬
        Collections.sort(comments, (c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()));

        if(voteOptions != null){
            this.voteOptions = new ArrayList<VoteOptionResponse>();
            for(VoteOption voteOption : voteOptions){
                VoteOptionResponse voteOptionResponse = new VoteOptionResponse(voteOption);
                this.voteOptions.add(voteOptionResponse);
            }
        }

        if(comments != null){
            this.comments = new ArrayList<CommentResponse>();
            for (Comment comment : comments) {
                CommentResponse commentResponse = new CommentResponse(comment);
                this.comments.add(commentResponse);
            }
        }

    }
}
