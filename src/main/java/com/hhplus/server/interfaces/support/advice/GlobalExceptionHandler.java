package com.hhplus.server.interfaces.support.advice;

import com.hhplus.server.domain.support.exception.ErrorCode;
import com.hhplus.server.domain.support.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = CommonException.class)
    public ResponseEntity<ErrorResponse> handleException(CommonException e) {
        switch (e.getErrorCode().getLogLevel()) {
            case ERROR -> log.error("CommonException : {}", e.getMessage(), e);
            case WARN -> log.warn("CommonException : {}", e.getMessage(), e);
            default -> log.info("CommonException : {}", e.getMessage(), e);
        }
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.getStatus(), errorCode.getStatus().name(), errorCode.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception: ", e);
        return ResponseEntity
                .status(500)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.name(), "에러가 발생했습니다."));
    }

}