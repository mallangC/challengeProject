package com.zerobase.challengeproject.comment.repository;

import com.zerobase.challengeproject.comment.entity.DietChallenge;

public interface DietCommentRepositoryCustom {

  DietChallenge searchDietChallengeByChallengeIdAndMemberId(Long challengeId, String loginId);
}
