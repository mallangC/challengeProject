package com.zerobase.challengeproject.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.challengeproject.comment.domain.dto.DietChallengeDto;
import com.zerobase.challengeproject.comment.entity.DietChallenge;
import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

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

  @Override
  public Page<DietChallengeDto> searchDietChallengeByChallengeId(int page, Long challengeId) {
    Pageable pageable = PageRequest.of(page, 20);

    Long total = queryFactory.select(dietChallenge.count())
            .from(dietChallenge)
            .where(dietChallenge.challenge.id.eq(challengeId))
            .fetchOne();

    if (total == null) {
      return new PageImpl<>(List.of(), pageable, 0);
    }

    List<DietChallenge> findDietChallenges = queryFactory.selectFrom(dietChallenge)
            .join(dietChallenge.challenge, challenge).fetchJoin()
            .join(dietChallenge.member, member).fetchJoin()
            .where(dietChallenge.challenge.id.eq(challengeId))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

    List<DietChallengeDto> dietChallengeDtos = findDietChallenges.stream()
            .map(DietChallengeDto::fromWithoutComments)
            .toList();

    return new PageImpl<>(dietChallengeDtos, pageable, total);
  }
}
