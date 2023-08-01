package com.spr.expost.util;


import lombok.Getter;

/**
 * 예외발생시 메시지 (spring Security 사용으로 인해 postman에서 보이지 않는 현상 있음)
 */
@Getter
public enum CommonErrorCode {

    INVALID_TOKEN(400, "토큰이 유효하지 않습니다."),
    UNAUTHORIZED_USER(400, "작성자만 수정/삭제 할 수 있습니다."),
    IN_USED_USERNAME(400, "중복된 username 입니다."),
    NOT_FOUND_USER(400, "회원을 찾을 수 없습니다."),
    NOT_FOUND_POST(400, "요청한 게시글이 존재하지 않습니다."),
    WRONG_PASSWORD(400, "비밀번호가 틀렸습니다."),
    NOT_FOUND_COMMENT(400, "작성한 댓글을 찾을 수 없습니다.");

    private int errorCode;
    private String errorMessage;

    CommonErrorCode(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
