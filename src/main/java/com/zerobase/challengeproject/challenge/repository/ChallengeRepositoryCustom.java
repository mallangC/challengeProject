package com.zerobase.challengeproject.challenge.repository;

import com.zerobase.challengeproject.challenge.entity.Challenge;

public interface ChallengeRepositoryCustom {

  Challenge searchChallengeById(Long challengeId);
}
