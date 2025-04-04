package com.zerobase.challengeproject.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.challengeproject.comment.entity.CoteComment;
import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

import static com.zerobase.challengeproject.comment.entity.QCoteComment.coteComment;
import static com.zerobase.challengeproject.member.entity.QMember.member;

@RequiredArgsConstructor
public class CoteCommentRepositoryCustomImpl implements CoteCommentRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public CoteComment searchCoteCommentById(Long coteCommentId) {

    CoteComment findCoteComment = queryFactory.selectFrom(coteComment)
            .join(coteComment.member, member).fetchJoin()
            .where(coteComment.id.eq(coteCommentId))
            .fetchOne();

    if (findCoteComment == null) {
      throw new CustomException(ErrorCode.NOT_FOUND_COTE_COMMENT);
    }

    return findCoteComment;
  }
}
