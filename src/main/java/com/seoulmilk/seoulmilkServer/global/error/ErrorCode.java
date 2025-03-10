package com.seoulmilk.seoulmilkServer.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //UNAUTHORIZED
    UNAUTHORIZED(403,"접근 권한이 없습니다."),

    //NOT_VALID
    NOT_VALID_ERROR(400, "잘못된 파라미터입니다."),

    //MEMBER
    MEMBER_NOT_FOUND(404, "존재하지 않는 사원입니다."),
    MEMBER_LOGIN_FAILED(401, "사원번호 또는 비밀번호가 잘못되었습니다."),
    MEMBER_ALREADY_REGISTERED(409, "이미 승인처리된 사원입니다"),

    //AGENCY
    AGENCY_EMAIL_INVALID(400,"잘못된 이메일입니다"),
    AGENCY_NOT_FOUND(404,"존재하지 않는 대리점입니다."),
    AGENCY_ALREADY_REGISTERED(409,"이미 회원가입된 대리점입니다."),
    AGENCY_DISAPPROVED(403,"관리자로부터 미승인된 대리점입니다."),
    AGENCY_NOT_VERIFIED(400, "이메일 인증처리가 완료되지 않았습니다."),
    AGENCY_LOGIN_FAILED(401, "아이디 또는 비밀번호가 잘못되었습니다."),

    //ADMIN
    MASTERKEY_INVALID(400,"유효하지 않은 마스터키입니다."),
    ADMIN_NOT_FOUND(404,"존재하지 않는 관리자입니다."),
    AGENCY_EMAIL_MISMATCH(400,"아이디 또는 이메일이 잘못되었습니다"),

    //ROLE
    ROLE_INVALID(400,"존재하지 않는 회원유형입니다."),

    //TOKEN
    TOKEN_EXPIRED(401, "만료된 토큰입니다"),
    TOKEN_INVALID(401, "유효하지 않은 토큰입니다."),

    //EMAIL
    MEMBER_EMAIL_MISMATCH(400,"사원번호 또는 이메일이 잘못되었습니다."),
    OTP_INVALID(400,"유효하지 않은 인증번호입니다."),
    MEMBER_UNAUTHORIZED(401,"인증되지 않았습니다."),

    //REDIS
    VERIFIED_MEMBER_NOT_FOUND(404,"인증된 사원이 존재하지 않습니다."),
    VERIFIED_AGENCY_NOT_FOUND(404,"인증된 대리점이 존재하지 않습니다."),

    //OCR
    OCR_REQUEST_FAILED(502, "OCR 요청에 실패했습니다."),
    OCR_PARSE_FAILED(502, "OCR 파싱에 실패했습니다."),
    FIELD_IS_EMPTY(400, "비어있는 필드입니다."),

    //NTS_TAX
    NTS_TAX_NOT_UPLOAD(400, "세금계산서 파일이 업로드 되지 않았습니다."),
    NTS_TAX_INVALID_FILE(400, "유효하지 않은 파일 형식입니다."),
    NTS_TAX_NOT_FOUND(404, "세금계산서를 찾을 수 없습니다."),
    NTS_TAX_UPDATE_UNAUTHORIZED(403, "세금 계산서 수정 권한이 없습니다."),
    NTS_TAX_DELETE_UNAUTHORIZED(403, "세금 계산서 삭제 권한이 없습니다."),
    NTS_TAX_VALIDATED(409, "이미 검증된 세금 계산서입니다"),
    WAITING_NOT_SELECTED(400, "WAITING 상태는 선택 불가능 합니다."),

    //S3
    FILE_IS_EMPTY(400, "비어있는 파일입니다."),
    FILE_IS_NOT_UPLOAD(400, "파일이 업로드 되지 않았습니다."),

    //CODEF
    CREATE_ACCESSTOKEN_CODEF_FAILED(500,"codef 토큰 요청 실패"),
    ENCODING_PASSWORD_FAILED(500,"인증서 비밀번호 암호화 실패"),
    VERIFY_TAX_INVOICE_FALIED(502,"전자세금계산서 검증 API 호출 실패");

    private final int code;
    private final String message;

}