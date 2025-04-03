package com.zerobase.challengeproject.comment.repository;

import com.zerobase.challengeproject.comment.entity.CoteChallenge;

import java.time.LocalDateTime;

public interface CoteChallengeRepositoryCustom {
  CoteChallenge searchCoteChallengeByStartAt(Long challengeId, String memberId, LocalDateTime startAt);
}
