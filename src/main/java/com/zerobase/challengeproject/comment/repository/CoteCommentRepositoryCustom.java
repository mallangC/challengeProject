package com.zerobase.challengeproject.comment.repository;

import com.zerobase.challengeproject.comment.entity.CoteComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CoteCommentRepositoryCustom {

  CoteComment searchCoteCommentById(Long coteCommentId);
}
