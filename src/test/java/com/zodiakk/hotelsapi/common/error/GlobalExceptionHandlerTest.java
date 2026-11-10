package com.zodiakk.hotelsapi.common.error;

import com.zodiakk.hotelsapi.common.exception.ConflictException;
import com.zodiakk.hotelsapi.common.exception.HotelNotFoundException;
import com.zodiakk.hotelsapi.common.exception.InvalidHistogramParamException;
import com.zodiakk.hotelsapi.common.exception.InvalidTimeZoneException;
import com.zodiakk.hotelsapi.common.message.ErrorMessages;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound() {
        ResponseEntity<ErrorResponseDto> response =
                handler.handleNotFound(new HotelNotFoundException("missing"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("missing");
    }

    @Test
    void handleConflict() {
        ResponseEntity<ErrorResponseDto> response =
                handler.handleConflict(new ConflictException("conflict"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getMessage()).isEqualTo("conflict");
    }

    @Test
    void handleBadRequest_invalidTimeZone() {
        ResponseEntity<ErrorResponseDto> response =
                handler.handleBadRequest(new InvalidTimeZoneException("bad zone"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).isEqualTo("bad zone");
    }

    @Test
    void handleBadRequest_invalidHistogramParam() {
        ResponseEntity<ErrorResponseDto> response =
                handler.handleBadRequest(new InvalidHistogramParamException("bad param"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void handleMethodArgumentNotValid() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "hotel");
        bindingResult.addError(new FieldError("hotel", "name", "required"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponseDto> response = handler.handleMethodArgumentNotValid(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).isEqualTo(ErrorMessages.VALIDATION_FAILED);
        assertThat(response.getBody().getFieldErrors()).hasSize(1);
        assertThat(response.getBody().getFieldErrors().getFirst().getField()).isEqualTo("name");
    }

    @Test
    void handleConstraintViolation() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("amenities[0]");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("required");

        ResponseEntity<ErrorResponseDto> response =
                handler.handleConstraintViolation(new ConstraintViolationException(Set.of(violation)));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getFieldErrors()).hasSize(1);
        assertThat(response.getBody().getFieldErrors().getFirst().getField()).isEqualTo("amenities[0]");
    }
}
