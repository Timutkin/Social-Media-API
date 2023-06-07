package ru.timutkin.socialmediaapi.api.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.timutkin.socialmediaapi.api.exception.*;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {InvalidRegistrationDataException.class, InvalidPostDataException.class})
    protected ResponseEntity<String> handleValidationException(ValidationException validationException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(validationException.getMessage());
    }

    @ExceptionHandler(value = {UserNotFoundException.class, PostNotFoundException.class})
    protected ResponseEntity<String> handleNotFoundException(NotFoundException validationException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(validationException.getMessage());
    }
}