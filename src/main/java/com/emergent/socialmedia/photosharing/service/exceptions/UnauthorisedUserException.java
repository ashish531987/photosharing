package com.emergent.socialmedia.photosharing.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorisedUserException extends RuntimeException{
    public UnauthorisedUserException(Long id){
        super("User "+id+" not found!");
    }
    public UnauthorisedUserException(String message) {
        super(message);
    }

    public UnauthorisedUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
