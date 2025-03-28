package com.zerobase.challengeproject.challenge.domain.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
public class BaseResponseDto<T> {
    private T data;
    private String message;
    private HttpStatus status;
    public BaseResponseDto(T data, String message, HttpStatus status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }
}
