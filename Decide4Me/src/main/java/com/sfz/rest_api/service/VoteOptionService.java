package com.sfz.rest_api.service;

import com.sfz.rest_api.model.VoteOption;
import java.util.List;

public interface VoteOptionService {
    VoteOption createVoteOption(VoteOption voteOption);
    VoteOption updateVoteOption(VoteOption voteOption);
    void deleteVoteOption(Long voteOptionId);
    VoteOption getVoteOptionById(Long voteOptionId);
}
