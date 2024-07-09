package com.sfz.rest_api.repository;

import com.sfz.rest_api.model.VoteParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteParticipantRepository extends JpaRepository<VoteParticipant, Long> {
    VoteParticipant findByUserIdAndPostId(String userId, Long postId);
    VoteParticipant findByVoteOptionIdAndUserId(Long voteOptionId, String userId);
}
