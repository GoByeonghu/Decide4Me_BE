package com.sfz.rest_api.repository;

import com.sfz.rest_api.model.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteOptionRepository extends JpaRepository<VoteOption, Long> {
}
