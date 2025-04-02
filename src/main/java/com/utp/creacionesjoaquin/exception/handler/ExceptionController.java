package com.utp.creacionesjoaquin.exception.handler;

import com.utp.creacionesjoaquin.commons.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.exception.customException.ResourceDuplicatedException;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus( HttpStatus.NOT_FOUND )
    @ExceptionHandler( ResourceNotFoundException.class )
    public ResponseWrapperDTO<String> handlerResourceNotFoundException(ResourceNotFoundException resourceNotFoundException){
        return ResponseWrapperDTO.<String>builder()
                .message(resourceNotFoundException.getMessage())
                .status(resourceNotFoundException.getStatus().name())
                .success( false)
                .content("RESOURCE INVALID")
                .build();
    }

    @ResponseStatus( HttpStatus.BAD_REQUEST )
    @ExceptionHandler( ConstraintViolationException.class )
    public ResponseWrapperDTO<Map<String, String>> handlerConstraintViolationException(ConstraintViolationException constraintViolationException){
        Map<String, String> errors = new HashMap<>();
        constraintViolationException.getConstraintViolations().forEach( error -> {
            String fieldName = error.getPropertyPath().toString().split("[.]")[2];
            errors.put(fieldName, error.getMessage());
        });
        return ResponseWrapperDTO.<Map<String, String>>builder()
                .message("Campos mal puestos")
                .status(HttpStatus.BAD_REQUEST.name())
                .success( false)
                .content( errors )
                .build();
    }

    @ResponseStatus( HttpStatus.BAD_REQUEST )
    @ExceptionHandler( MethodArgumentNotValidException.class )
    public ResponseWrapperDTO<Map<String, String>> handlerMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        Map<String, String> errors = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach( error -> {
            FieldError fieldError = ((FieldError) error);
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return ResponseWrapperDTO.<Map<String, String>>builder()
                .message("Campos mal puestos")
                .status(HttpStatus.BAD_REQUEST.name())
                .success( false)
                .content( errors )
                .build();
    }

    @ResponseStatus( HttpStatus.BAD_REQUEST )
    @ExceptionHandler( ResourceDuplicatedException.class )
    public ResponseWrapperDTO<String> handlerResourceDuplicatedException(ResourceDuplicatedException resourceDuplicatedException){
        return ResponseWrapperDTO.<String>builder()
                .message( "Recurso ya existente" )
                .status(HttpStatus.BAD_REQUEST.name())
                .success( false)
                .content( resourceDuplicatedException.getMessage() )
                .build();
    }

    @ResponseStatus( HttpStatus.UNAUTHORIZED )
    @ExceptionHandler( MalformedJwtException.class )
    public ResponseWrapperDTO<String> handlerMalformedJwtException(MalformedJwtException malformedJwtException){
        return ResponseWrapperDTO.<String>builder()
                .message( "Token mal formado" )
                .status(HttpStatus.UNAUTHORIZED.name())
                .success( false )
                .content( malformedJwtException.getMessage() )
                .build();
    }

    @ResponseStatus( HttpStatus.UNAUTHORIZED )
    @ExceptionHandler( SignatureException.class )
    public ResponseWrapperDTO<String> handleSignatureException(SignatureException SignatureException){
        return ResponseWrapperDTO.<String>builder()
                .message( "Token con firma invalida" )
                .status(HttpStatus.UNAUTHORIZED.name())
                .success( false )
                .content( SignatureException.getMessage() )
                .build();
    }

    @ResponseStatus( HttpStatus.UNAUTHORIZED )
    @ExceptionHandler( WeakKeyException.class )
    public ResponseWrapperDTO<String> handlerWeakKeyException(WeakKeyException weakKeyException){
        return ResponseWrapperDTO.<String>builder()
                .message( "Token mal firmado" )
                .status(HttpStatus.UNAUTHORIZED.name())
                .success( false )
                .content( weakKeyException.getMessage() )
                .build();
    }

    @ResponseStatus( HttpStatus.UNAUTHORIZED )
    @ExceptionHandler( ExpiredJwtException.class )
    public ResponseWrapperDTO<String> handlerExpiredJwtException(ExpiredJwtException expiredJwtException){
        return ResponseWrapperDTO.<String>builder()
                .message( "Token expirado" )
                .status(HttpStatus.UNAUTHORIZED.name())
                .success( false )
                .content( expiredJwtException.getMessage() )
                .build();
    }

    @ResponseStatus( HttpStatus.FORBIDDEN )
    @ExceptionHandler( AccessDeniedException.class )
    public ResponseWrapperDTO<String> handlerAccessDeniedException(AccessDeniedException accessDeniedException){
        return ResponseWrapperDTO.<String>builder()
                .message( "No tiene los permisos para realizar esta accion")
                .status(HttpStatus.FORBIDDEN.name())
                .success( false )
                .content( accessDeniedException.getMessage() )
                .build();
    }
}

