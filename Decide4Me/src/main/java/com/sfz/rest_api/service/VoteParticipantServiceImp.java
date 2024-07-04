package com.sfz.rest_api.service;

import com.sfz.rest_api.model.VoteParticipant;
import com.sfz.rest_api.repository.VoteParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class VoteParticipantServiceImp implements VoteParticipantService {

    @Autowired
    private VoteParticipantRepository voteParticipantRepository;

    @Override
    public VoteParticipant createVoteParticipant(VoteParticipant voteParticipant) {
        // Implement logic to create vote participant
        return voteParticipantRepository.save(voteParticipant);
    }

    @Override
    public VoteParticipant updateVoteParticipant(VoteParticipant voteParticipant) {
        // Implement logic to update vote participant
        return voteParticipantRepository.save(voteParticipant);
    }

    @Override
    public void deleteVoteParticipant(Long voteParticipantId) {
        // Implement logic to delete vote participant
        voteParticipantRepository.deleteById(voteParticipantId);
    }

    @Override
    public VoteParticipant getVoteParticipantById(Long voteParticipantId) {
        // Implement logic to get vote participant by ID
        return voteParticipantRepository.findById(voteParticipantId).orElse(null);
    }

    // Additional methods can be implemented here
}