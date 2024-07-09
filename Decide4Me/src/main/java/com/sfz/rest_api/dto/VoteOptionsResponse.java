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

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class VoteOptionsResponse {
    private List<VoteOptionResponse> voteOptionResponses;

    public VoteOptionsResponse(List<VoteOption> voteOptions){
        this.voteOptionResponses = new ArrayList<VoteOptionResponse>();
        for(VoteOption voteOption : voteOptions){
            VoteOptionResponse voteOptionResponse = new VoteOptionResponse(voteOption);
            this.voteOptionResponses.add(voteOptionResponse);
        }
    }

}
