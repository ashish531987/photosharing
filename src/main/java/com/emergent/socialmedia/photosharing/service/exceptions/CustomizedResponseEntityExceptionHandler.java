package com.emergent.socialmedia.photosharing.service.exceptions;

import com.emergent.socialmedia.photosharing.resources.dto.response.ExceptionResponseContainerDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponseContainerDTO> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponseContainerDTO ExceptionResponseContainerDTO = new ExceptionResponseContainerDTO(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(ExceptionResponseContainerDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MediaNotFoundException.class)
    public final ResponseEntity<ExceptionResponseContainerDTO> handleDeviceNotFoundException(
            MediaNotFoundException ex,
            WebRequest request) {
        ExceptionResponseContainerDTO ExceptionResponseContainerDTO = new ExceptionResponseContainerDTO(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(ExceptionResponseContainerDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileStorageException.class)
    public final ResponseEntity<ExceptionResponseContainerDTO> handleDeviceAlreadyAllocatedException(
            FileStorageException ex,
            WebRequest request) {
        ExceptionResponseContainerDTO ExceptionResponseContainerDTO = new ExceptionResponseContainerDTO(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(ExceptionResponseContainerDTO, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<ExceptionResponseContainerDTO> handleDeviceAlreadyAllocatedException(
            UserNotFoundException ex,
            WebRequest request) {
        ExceptionResponseContainerDTO ExceptionResponseContainerDTO = new ExceptionResponseContainerDTO(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(ExceptionResponseContainerDTO, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UnauthorisedUserException.class)
    public final ResponseEntity<ExceptionResponseContainerDTO> handleDeviceAlreadyAllocatedException(
            UnauthorisedUserException ex,
            WebRequest request) {
        ExceptionResponseContainerDTO ExceptionResponseContainerDTO = new ExceptionResponseContainerDTO(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(ExceptionResponseContainerDTO, HttpStatus.NOT_FOUND);
    }

}
