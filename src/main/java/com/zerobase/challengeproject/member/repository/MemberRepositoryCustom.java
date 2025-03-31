package com.zerobase.challengeproject.member.repository;

import com.zerobase.challengeproject.member.entity.Member;

import java.time.LocalDateTime;

public interface MemberRepositoryCustom {
  Member searchByEmailAndAccountDetailsToDate(String email, LocalDateTime searchByDate);

  Member searchByEmailAndAccountDetailId(String email, Long accountId);
}
