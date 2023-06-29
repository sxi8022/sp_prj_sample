package com.spr.expost.exception;

import com.spr.expost.util.CommonErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *  예외 발생시 튕기도록 처리
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR) // response로 들어가는 에러 코드를 400으로 통일
public class ExtException extends RuntimeException {

    private final CommonErrorCode errorCode;

    public ExtException(CommonErrorCode errorCode, Throwable cause) {
        super(errorCode.getErrorMessage(), cause, false, false);
        this.errorCode = errorCode;
    }

    public CommonErrorCode getErrorCode() {
        return this.errorCode;
    }
}