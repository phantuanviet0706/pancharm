package com.example.pancharm.exception;

import com.example.pancharm.constant.ErrorCode;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class AppException extends RuntimeException {
    ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
