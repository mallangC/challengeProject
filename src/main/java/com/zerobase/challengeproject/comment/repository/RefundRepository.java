package com.zerobase.challengeproject.comment.repository;

import com.zerobase.challengeproject.comment.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> , RefundRepositoryCustom{
  boolean existsByAccountDetail_Id(Long accountDetailId);
}
