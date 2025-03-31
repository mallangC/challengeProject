package com.zerobase.challengeproject.challenge.repository;

import com.zerobase.challengeproject.challenge.entity.MemberChallenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberChallengeRepository extends JpaRepository<MemberChallenge, Long> {
    Page<MemberChallenge> findByMemberId(Long memberId, Pageable pageable);
}
