package com.zerobase.challengeproject.comment.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.challengeproject.comment.domain.dto.RefundDto;
import com.zerobase.challengeproject.comment.entity.Refund;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.zerobase.challengeproject.comment.entity.QRefund.refund;

@RequiredArgsConstructor
public class RefundRepositoryCustomImpl implements RefundRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<RefundDto> searchAllRefund(int page, LocalDateTime startAt, Boolean isDone, Boolean isRefunded) {
    Pageable pageable = PageRequest.of(page, 20);

    BooleanExpression whereClause = refund.createdAt.loe(LocalDateTime.now());
    if (startAt != null) {
      whereClause = refund.createdAt.between(startAt, LocalDateTime.now());
    }

    Long total = queryFactory.select(refund.count())
            .from(refund)
            .where(whereClause
                    .and(isRefunded == null ? null : refund.isRefunded.eq(isRefunded))
                    .and(isDone == null ? null : refund.isDone.eq(isDone)))
            .fetchOne();

    if (total == null) {
      return new PageImpl<>(List.of(), pageable, 0);
    }

    List<Refund> findRefunds = queryFactory.selectFrom(refund)
            .join(refund.member).fetchJoin()
            .join(refund.accountDetail).fetchJoin()
            .where(whereClause
                    .and(isRefunded == null ? null : refund.isRefunded.eq(isRefunded))
                    .and(isDone == null ? null : refund.isDone.eq(isDone)))
            .orderBy(refund.createdAt.desc())
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

    List<RefundDto> refundDtos = findRefunds.stream()
            .map(RefundDto::from)
            .toList();

    return new PageImpl<>(refundDtos, pageable, total);
  }
}
