package com.masagreen.RentalUnitsManagement.exceptions;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionsHadler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("logging {}", ex.getMessage());
        ExceptionsObject exceptionsObject = ExceptionsObject.withSingleMessage(new Date(), ex.getMessage(),
                HttpStatus.METHOD_NOT_ALLOWED.value());
        return new ResponseEntity<>(exceptionsObject, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("logging {}", ex.getMessage());
        Map<String, String> eMap = new HashMap<>();

        List<ObjectError> errors = ex.getBindingResult().getAllErrors();

        for (ObjectError error : errors) {
            if (error instanceof FieldError) {
                eMap.put(((FieldError) error).getField(), error.getDefaultMessage());
            } else {
                eMap.put("message", error.getDefaultMessage());
            }

        }
        ExceptionsObject exceptionsObject = ExceptionsObject.withManyMessages(new Date(), eMap,
                HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionsObject, HttpStatus.BAD_REQUEST);

    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatusCode status, WebRequest request) {
        log.error("logging {}", ex.getMessage());
        ExceptionsObject exceptionsObject = ExceptionsObject.withSingleMessage(new Date(), ex.getMessage(),
                HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionsObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongEmailValidationCode.class)
    public ResponseEntity<Object> handleWrongEmailValidationCode(WrongEmailValidationCode ex) {
        log.error("logging {}", ex.getMessage());
        ExceptionsObject exceptionsObject = ExceptionsObject.withSingleMessage(new Date(), ex.getMessage(),
                HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionsObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error("logging {}", ex.getMessage());
        ExceptionsObject exceptionsObject = ExceptionsObject.withSingleMessage(new Date(), ex.getMessage(),
                HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(exceptionsObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<Object> handleInsufficientAuthenticationException(InsufficientAuthenticationException ex) {
        log.error("logging {}", ex.getMessage());
        ExceptionsObject exceptionsObject = ExceptionsObject.withSingleMessage(new Date(), ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(exceptionsObject, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<Object> handleEntityExistsException(EntityExistsException ex) {
        log.error("logging {}", ex.getMessage());
        ExceptionsObject exceptionsObject = ExceptionsObject.withSingleMessage(new Date(), ex.getMessage(),
                HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(exceptionsObject, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<Object> handlePasswordMismatchException(PasswordMismatchException ex) {
        log.error("logging {}", ex.getMessage());
        ExceptionsObject exceptionsObject = ExceptionsObject.withSingleMessage(new Date(), ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(exceptionsObject, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedxception(AccessDeniedException ex) {
        log.error("logging {}", ex.getMessage());
        ExceptionsObject exceptionsObject = ExceptionsObject.withSingleMessage(new Date(), ex.getMessage(),
                HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(exceptionsObject, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserNotValidatedException.class)
    public ResponseEntity<Object> handleUserNotValidatedException(UserNotValidatedException ex) {
        log.error("logging {}", ex.getMessage());
        ExceptionsObject exceptionsObject = ExceptionsObject.withSingleMessage(new Date(), ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(exceptionsObject, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex) {
        log.error("logging {}", ex.getMessage());
        ExceptionsObject exceptionsObject = ExceptionsObject.withSingleMessage(new Date(), ex.getMessage(),
                HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionsObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("logging {}", ex.getMessage());
        ExceptionsObject exceptionsObject = ExceptionsObject.withSingleMessage(new Date(), ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(exceptionsObject, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalArgumentException ex) {
        log.error("logging {}", ex.getMessage());
        ExceptionsObject exceptionsObject = ExceptionsObject.withSingleMessage(new Date(), ex.getMessage(),
                HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionsObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<Object> handleMalformedJwtException(MalformedJwtException ex) {
        log.error("logging {}", ex.getMessage());
        ExceptionsObject exceptionsObject = ExceptionsObject.withSingleMessage(new Date(), ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(exceptionsObject, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Object> handleSignatureException(SignatureException ex) {
        log.error("logging {}", ex.getMessage());
        ExceptionsObject exceptionsObject = ExceptionsObject.withSingleMessage(new Date(), ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(exceptionsObject, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DeletionNotAllowedCurrentlyException.class)
    public ResponseEntity<Object> handleDeletionNotAllowedCurrentlyException(DeletionNotAllowedCurrentlyException ex) {
        log.error("logging {}", ex.getMessage());
        ExceptionsObject exceptionsObject = ExceptionsObject.withSingleMessage(new Date(), ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(exceptionsObject, HttpStatus.UNAUTHORIZED);
    }

}
