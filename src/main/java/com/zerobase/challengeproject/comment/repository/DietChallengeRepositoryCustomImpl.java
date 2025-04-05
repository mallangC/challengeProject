package com.zerobase.challengeproject.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.challengeproject.comment.entity.DietChallenge;
import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

import static com.zerobase.challengeproject.challenge.entity.QChallenge.challenge;
import static com.zerobase.challengeproject.comment.entity.QDietChallenge.dietChallenge;
import static com.zerobase.challengeproject.comment.entity.QDietComment.dietComment;
import static com.zerobase.challengeproject.member.entity.QMember.member;

@RequiredArgsConstructor
public class DietChallengeRepositoryCustomImpl implements DietChallengeRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public DietChallenge searchDietChallengeByChallengeIdAndLoginId(Long challengeId, String loginId) {

    DietChallenge findDietChallenge = queryFactory.selectFrom(dietChallenge)
            .join(dietChallenge.challenge, challenge).fetchJoin()
            .join(dietChallenge.member, member).fetchJoin()
            .leftJoin(dietChallenge.comments, dietComment).fetchJoin()
            .where(dietChallenge.challenge.id.eq(challengeId)
                    .and(dietChallenge.member.memberId.eq(loginId)))
            .fetchOne();

    if (findDietChallenge == null) {
      throw new CustomException(ErrorCode.NOT_FOUND_DIET_CHALLENGE);
    }

    return findDietChallenge;
  }
}
