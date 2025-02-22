package com.seoulmilk.seoulmilkServer.global.error;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private final boolean success;
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;


    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(
            errorCode.getStatus(),
            errorCode.getMessage()
        );
    }

    public ErrorResponse(int status, String message) {
        this.success = false;  // 항상 실패로 간주
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }


}
