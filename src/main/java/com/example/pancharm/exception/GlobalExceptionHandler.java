package com.example.pancharm.exception;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import java.util.Objects;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.UnexpectedTypeException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.response.auth.ApiResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDenied(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodException(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;

        try {
            errorCode = ErrorCode.valueOf(enumKey);

            var constraintViolation =
                    exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();
        } catch (IllegalArgumentException e) {

        }

        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(
                Objects.nonNull(attributes)
                        ? mapAttribute(errorCode.getMessage(), attributes)
                        : errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            message = message.replace("{" + key + "}", String.valueOf(value));
        }

        return message;
    }

    @ExceptionHandler(value = UnexpectedTypeException.class)
    ResponseEntity<ApiResponse> handleUnexpectedTypeException(UnexpectedTypeException exception) {
        log.info(exception.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.builder()
                        .message(exception.getLocalizedMessage())
                        .build());
    }

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    ResponseEntity<ApiResponse> handleSQLIntegrityConstraintViolationException(
            SQLIntegrityConstraintViolationException exception) {

        return ResponseEntity.status(exception.getErrorCode())
                .body(ApiResponse.builder()
                        .result(exception.getLocalizedMessage())
                        .build());
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiResponse> handlingMultipartException(MultipartException exception) {
        System.err.println("Multipart request failed: " + exception.getMessage());

        ErrorCode errorCode = ErrorCode.MULTIPART_INVALID;

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message("Yêu cầu tải tệp tin không hợp lệ hoặc bị lỗi cú pháp.")
                        .build());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse> handlingJwtException(JwtException exception) {
        ErrorCode errorCode = ErrorCode.MULTIPART_INVALID;

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message("Token không hợp lệ")
                        .build());
    }
}
