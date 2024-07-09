package com.sfz.rest_api.dto;
import com.sfz.rest_api.model.Post;
import com.sfz.rest_api.model.Comment;
import com.sfz.rest_api.model.User;
import com.sfz.rest_api.model.VoteOption;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostRequest {
    private String user_id;//private User user; //닉네임만
    private String title;
    private String content;
    private LocalDateTime created_at;//createdAt; //이름 변경
    private LocalDateTime updated_at;//updatedAt; //이름 변경
    private LocalDateTime deadline;
    private List<VoteOption> voteOptions;
}
