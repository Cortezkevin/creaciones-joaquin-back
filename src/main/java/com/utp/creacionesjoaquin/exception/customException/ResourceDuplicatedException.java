package com.utp.creacionesjoaquin.exception.customException;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ResourceDuplicatedException extends Exception{
    @Getter
    private HttpStatus status;
    public ResourceDuplicatedException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
