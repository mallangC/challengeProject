package com.zerobase.challengeproject.comment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageDto<T> {
  private List<T> content;
  private long totalElements;
  private int totalPages;
  private int number;
  private int size;

  public static <T> PageDto<T> from(Page<T> page){
    return PageDto.<T>builder()
            .content(page.getContent())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .number(page.getNumber())
            .size(page.getSize())
            .build();
  }

}
