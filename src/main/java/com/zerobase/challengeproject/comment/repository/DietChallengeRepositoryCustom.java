package com.zerobase.challengeproject.comment.repository;

import com.zerobase.challengeproject.comment.entity.DietChallenge;

public interface DietChallengeRepositoryCustom {

  DietChallenge searchDietChallengeByChallengeIdAndLoginId(Long challengeId, String loginId);
}
