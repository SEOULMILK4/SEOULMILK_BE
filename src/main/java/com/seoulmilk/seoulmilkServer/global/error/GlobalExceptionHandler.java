package com.seoulmilk.seoulmilkServer.global.error;

import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleRuntimeException(BusinessException e) {

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(errorResponse);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {

        return ResponseEntity
            .status(e.getStatusCode())
            .body(new ErrorResponse(ErrorCode.NOT_VALID_ERROR.getStatus(),
                ErrorCode.NOT_VALID_ERROR.getMessage()));
    }

}
