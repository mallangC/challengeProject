package com.zerobase.challengeproject.comment.repository;

import com.zerobase.challengeproject.comment.entity.AccountDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDetailRepository extends JpaRepository<AccountDetail, Long>{
}
