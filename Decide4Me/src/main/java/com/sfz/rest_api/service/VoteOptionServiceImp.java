package com.sfz.rest_api.service;

import com.sfz.rest_api.model.VoteOption;
import com.sfz.rest_api.repository.VoteOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VoteOptionServiceImp implements VoteOptionService {

    @Autowired
    private VoteOptionRepository voteOptionRepository;

    @Override
    public VoteOption createVoteOption(VoteOption voteOption) {
        // Implement logic to create vote option
        return voteOptionRepository.save(voteOption);
    }

    @Override
    public VoteOption updateVoteOption(VoteOption voteOption) {
        // Implement logic to update vote option
        return voteOptionRepository.save(voteOption);
    }

    @Override
    public void deleteVoteOption(Long voteOptionId) {
        // Implement logic to delete vote option
        voteOptionRepository.deleteById(voteOptionId);
    }

    @Override
    public VoteOption getVoteOptionById(Long voteOptionId) {
        // Implement logic to get vote option by ID
        return voteOptionRepository.findById(voteOptionId).orElse(null);
    }

    // Additional methods can be implemented here
}
