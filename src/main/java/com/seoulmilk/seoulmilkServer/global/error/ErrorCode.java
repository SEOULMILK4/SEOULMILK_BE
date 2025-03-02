package com.seoulmilk.seoulmilkServer.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //NOT_VALID
    NOT_VALID_ERROR(404, "잘못된 파라미터입니다."),

    //MEMBER
    MEMBER_NOT_FOUND(404, "존재하지 않는 회원입니다."),

    LOGIN_FAILED(401, "사원번호 또는 비밀번호가 잘못되었습니다."),

    //TOKEN
    TOKEN_EXPIRED(400, "만료된 토큰입니다"),
    TOKEN_INVALID(400, "유효하지 않은 토큰입니다."),

    //EMAIL
    MEMBER_EMAIL_MISMATCH(400,"사원번호 또는 이메일이 잘못되었습니다."),
    OTP_INVALID(400,"유효하지 않은 인증번호입니다."),
    MEMBER_UNAUTHORIZED(401,"인증되지 않았습니다."),

    //REDIS
    VERIFIED_MEMBER_NOT_FOUND(404,"인증된 사원이 존재하지 않습니다."),

    //OCR
    OCR_REQUEST_FAILED(500, "OCR 요청에 실패했습니다."),
    OCR_PARSE_FAILED(500, "OCR 파싱에 실패했습니다."),
    FIELD_IS_EMPTY(400, "비어있는 필드입니다."),

    //NTS_TAX
    NTS_TAX_NOT_UPLOAD(400, "파일이 업로드 되지 않았습니다."),
    NTS_TAX_INVALID_FILE(400, "유효하지 않은 파일 형식입니다."),
    NTS_TAX_NOT_FOUND(404, "세금계산서를 찾을 수 없습니다."),
    NTS_TAX_UPDATE_UNAUTHORIZED(401, "세금계산서 수정 권한이 없습니다."),
    NTS_TAX_DELETE_UNAUTHORIZED(401, "세금계산서 삭제 권한이 없습니다.");

    private final int code;
    private final String message;

}