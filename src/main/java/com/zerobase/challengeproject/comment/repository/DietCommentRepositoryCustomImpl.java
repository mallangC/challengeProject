package com.zerobase.challengeproject.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.challengeproject.comment.entity.DietComment;
import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

import static com.zerobase.challengeproject.challenge.entity.QChallenge.challenge;
import static com.zerobase.challengeproject.comment.entity.QDietChallenge.dietChallenge;
import static com.zerobase.challengeproject.comment.entity.QDietComment.dietComment;
import static com.zerobase.challengeproject.member.entity.QMember.member;

@RequiredArgsConstructor
public class DietCommentRepositoryCustomImpl implements DietCommentRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public DietComment searchDietCommentById(Long commentId) {

    DietComment findDietComment = queryFactory.selectFrom(dietComment)
            .join(dietComment.dietChallenge, dietChallenge).fetchJoin()
            .join(dietComment.dietChallenge.challenge, challenge).fetchJoin()
            .join(dietComment.member, member).fetchJoin()
            .where(dietComment.id.eq(commentId))
            .fetchOne();

    if (findDietComment == null) {
      throw new CustomException(ErrorCode.NOT_FOUND_DIET_COMMENT);
    }

    return findDietComment;
  }
}

