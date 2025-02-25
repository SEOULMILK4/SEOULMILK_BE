package com.seoulmilk.seoulmilkServer.global.error;

import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ApiResponse<ErrorResponse> handleRuntimeException(BusinessException e) {

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);

        return ApiResponse.error(errorResponse);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ApiResponse<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {

        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.NOT_VALID_ERROR.getCode(),
            ErrorCode.NOT_VALID_ERROR.getMessage());

        return ApiResponse.error(errorResponse);
    }

}
