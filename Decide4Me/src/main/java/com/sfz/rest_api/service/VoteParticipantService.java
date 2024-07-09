package com.sfz.rest_api.service;

import com.sfz.rest_api.model.VoteParticipant;
import java.util.List;

public interface VoteParticipantService {
    VoteParticipant createVoteParticipant(VoteParticipant voteParticipant);
    VoteParticipant updateVoteParticipant(VoteParticipant voteParticipant);
    void deleteVoteParticipant(Long voteParticipantId);
    VoteParticipant getVoteParticipantById(Long voteParticipantId);
    VoteParticipant getVoteParticipantByUserIdAndPostId(String userId, Long postId);
    VoteParticipant getVoteParticipantByUserIdAndVoteOptionId(String userId, Long voteOptionId);
}
