package co.com.crediya.api.util;


import co.com.crediya.api.dto.ApiErrorDTO;
import co.com.crediya.api.exception.MissingInvalidAuthHeaderException;
import co.com.crediya.model.exception.ApprovedReportNotSavedException;
import co.com.crediya.model.exception.InvalidTokenException;
import co.com.crediya.model.exception.NoReportsFoundException;
import co.com.crediya.model.exception.NotAdminRoleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandler {


    @ExceptionHandler({NoReportsFoundException.class, ApprovedReportNotSavedException.class})
    public Mono<ResponseEntity<ApiErrorDTO>> handleBusinessReportExceptions(RuntimeException ex) {
        return Mono.just(ResponseEntity.badRequest().body(new ApiErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Error with reports",
                ex.getMessage(),
                LocalDateTime.now().toString()
        )));
    }

    @ExceptionHandler({NotAdminRoleException.class})
    public Mono<ResponseEntity<ApiErrorDTO>> handleBusinessValidationExceptions(RuntimeException ex) {
        return Mono.just(ResponseEntity.badRequest().body(new ApiErrorDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                ex.getMessage(),
                LocalDateTime.now().toString()
        )));
    }

    @ExceptionHandler({MissingInvalidAuthHeaderException.class, InvalidTokenException.class})
    public Mono<ResponseEntity<ApiErrorDTO>> handleAuthorizationValidationExceptions(RuntimeException ex) {
        return Mono.just(ResponseEntity.badRequest().body(new ApiErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Missing Authorization",
                ex.getMessage(),
                LocalDateTime.now().toString()
        )));
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorDTO> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorDTO(
                        HttpStatus.BAD_REQUEST.value(),
                        "Not Found",
                        ex.getMessage(),
                        LocalDateTime.now().toString()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorDTO(
                        HttpStatus.BAD_REQUEST.value(),
                        "Not Found",
                        ex.getMessage(),
                        LocalDateTime.now().toString()));
    }
}