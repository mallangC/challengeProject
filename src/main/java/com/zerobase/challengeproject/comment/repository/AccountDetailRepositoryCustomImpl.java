package com.zerobase.challengeproject.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.challengeproject.comment.domain.dto.AccountDetailDto;
import com.zerobase.challengeproject.comment.entity.AccountDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.zerobase.challengeproject.comment.entity.QAccountDetail.accountDetail;

@RequiredArgsConstructor
public class AccountDetailRepositoryCustomImpl implements AccountDetailRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<AccountDetailDto> searchAllAccountDetail(int page, String userId) {
    Pageable pageable = PageRequest.of(page, 20);

    Long total = queryFactory.select(accountDetail.count())
            .from(accountDetail)
            .where(accountDetail.member.memberId.eq(userId))
            .orderBy(accountDetail.createdAt.desc())
            .fetchOne();

    if (total == null) {
      return new PageImpl<>(List.of(), pageable, 0);
    }

    List<AccountDetail> accountDetails = queryFactory.selectFrom(accountDetail)
            .where(accountDetail.member.memberId.eq(userId))
            .orderBy(accountDetail.createdAt.desc())
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

    List<AccountDetailDto> accountDetailDtos = accountDetails.stream()
            .map(AccountDetailDto::from)
            .toList();

    return new PageImpl<>(accountDetailDtos, pageable, total);
  }
}
