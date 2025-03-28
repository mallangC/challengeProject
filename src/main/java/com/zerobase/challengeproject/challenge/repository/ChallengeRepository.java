package com.zerobase.challengeproject.challenge.repository;


import com.zerobase.challengeproject.challenge.entity.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    Page<Challenge> findAll(Pageable pageable);


    /**
     * 사용자와 조인후 사용자아이디로 파라미터 변경예정
     */
    Page<Challenge> findById(Long id, Pageable pageable);
}
