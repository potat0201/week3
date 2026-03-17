package com.example.demo.exception;

import com.example.demo.response.ApiResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<String> handleValidationException(MethodArgumentNotValidException ex){

        String message = ex.getBindingResult()
                .getFieldError()
                .getDefaultMessage();
        log.warn("Dữ liệu đầu vào không hợp lệ");
        return new ApiResponse<>(1, message, null);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ApiResponse<String> handleUserNotFound(UserNotFoundException ex){
        log.error("Lỗi nghiệp vụ: {}", ex.getMessage());
        return new ApiResponse<>(1, ex.getMessage(), null);
    }

}