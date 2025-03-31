package com.zerobase.challengeproject.comment.repository;

import com.zerobase.challengeproject.comment.domain.dto.AccountDetailDto;
import org.springframework.data.domain.Page;

public interface AccountDetailRepositoryCustom {
  Page<AccountDetailDto> searchAllAccountDetail(int page, String userId);

}
