package com.zodiakk.hotelsapi.common.error;

import com.zodiakk.hotelsapi.common.exception.ConflictException;
import com.zodiakk.hotelsapi.common.exception.HotelNotFoundException;
import com.zodiakk.hotelsapi.common.exception.InvalidHistogramParamException;
import com.zodiakk.hotelsapi.common.exception.InvalidTimeZoneException;
import com.zodiakk.hotelsapi.common.message.ErrorMessages;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HotelNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(HotelNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDto> handleConflict(ConflictException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler({InvalidTimeZoneException.class, InvalidHistogramParamException.class})
    public ResponseEntity<ErrorResponseDto> handleBadRequest(RuntimeException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<FieldErrorDto> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> FieldErrorDto.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build())
                .toList();
        return build(HttpStatus.BAD_REQUEST, ErrorMessages.VALIDATION_FAILED, fieldErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(ConstraintViolationException ex) {
        List<FieldErrorDto> fieldErrors = ex.getConstraintViolations().stream()
                .map(violation -> FieldErrorDto.builder()
                        .field(violation.getPropertyPath().toString())
                        .message(violation.getMessage())
                        .build())
                .toList();
        return build(HttpStatus.BAD_REQUEST, ErrorMessages.VALIDATION_FAILED, fieldErrors);
    }

    private ResponseEntity<ErrorResponseDto> build(HttpStatus status, String message, List<FieldErrorDto> fieldErrors) {
        ErrorResponseDto body = ErrorResponseDto.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .fieldErrors(fieldErrors)
                .build();
        return ResponseEntity.status(status).body(body);
    }
}
