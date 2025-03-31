package com.zerobase.challengeproject.account.repository;

import com.zerobase.challengeproject.account.domain.dto.AccountDetailDto;
import org.springframework.data.domain.Page;

public interface AccountDetailRepositoryCustom {
  Page<AccountDetailDto> searchAllAccountDetail(int page, String userId);

}
