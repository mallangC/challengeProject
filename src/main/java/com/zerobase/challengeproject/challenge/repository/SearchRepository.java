package com.zerobase.challengeproject.challenge.repository;


import com.zerobase.challengeproject.challenge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends JpaRepository<Challenge, Long> {


}
