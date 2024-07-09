package com.sfz.rest_api.dto;

import com.sfz.rest_api.model.Comment;
import com.sfz.rest_api.model.Post;
import com.sfz.rest_api.model.VoteOption;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VoteOptionResponse {
    private Long id;
    private Long post_id;
    private String title;
    private int index;
    private Long count;

    public VoteOptionResponse(VoteOption voteOption){
        this.setId(voteOption.getId());
        this.setPost_id(voteOption.getPost().getId());
        this.setTitle(voteOption.getTitle());
        this.setIndex(voteOption.getIndex());
        this.setCount(voteOption.getCount());
    }
}
