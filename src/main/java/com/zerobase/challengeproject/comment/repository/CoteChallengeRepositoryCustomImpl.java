package com.zerobase.challengeproject.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.challengeproject.comment.entity.CoteChallenge;
import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static com.zerobase.challengeproject.comment.entity.QCoteChallenge.coteChallenge;
import static com.zerobase.challengeproject.comment.entity.QCoteComment.coteComment;


@RequiredArgsConstructor
public class CoteChallengeRepositoryCustomImpl implements CoteChallengeRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public CoteChallenge searchCoteChallengeByStartAt(Long challengeId, String memberId, LocalDateTime startAt) {
    //TODO 쿼리문이 3번 실행됨 수정
    CoteChallenge findCoteChallenge = queryFactory.selectFrom(coteChallenge)
            .leftJoin(coteChallenge.comments, coteComment).fetchJoin()
            .where(coteChallenge.challenge.id.eq(challengeId)
                    .and(coteChallenge.startAt.eq(startAt)))
            .fetchOne();

    if (findCoteChallenge == null) {
      throw new CustomException(ErrorCode.NOT_FOUND_COTE_CHALLENGE);
    }

    boolean isAlreadyComment = findCoteChallenge.getComments().stream()
            .anyMatch(comment ->
                    comment.getMember().getMemberId().equals(memberId));

    if (isAlreadyComment) {
      throw new CustomException(ErrorCode.ALREADY_ADDED_COMMENT_TODAY);
    }

    return findCoteChallenge;
  }
}
