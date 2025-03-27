package com.zerobase.challengeproject.member.repository;

import com.zerobase.challengeproject.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    Optional<Member> findByEmailAuthKey(String emailAuthKey);
}
