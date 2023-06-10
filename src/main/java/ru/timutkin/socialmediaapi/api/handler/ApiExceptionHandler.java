package ru.timutkin.socialmediaapi.api.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.timutkin.socialmediaapi.api.exception.*;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {
            InvalidRegistrationDataException.class,
            InvalidPostDataException.class})
    protected ResponseEntity<String> handleValidationException(ValidationException validationException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(validationException.getMessage());
    }

    @ExceptionHandler(value = {
            PostNotFoundException.class,
            FriendRequestNotFoundException.class,
            UserNotFoundException.class,
            ImageNotFoundException.class})
    protected ResponseEntity<String> handleNotFoundException(NotFoundException validationException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(validationException.getMessage());
    }

    @ExceptionHandler(value = {FriendRequestAlreadyExistsException.class, MessageRequestAlreadyExistsException.class})
    protected ResponseEntity<String> handleAlreadyExistsException(AlreadyExistsException alreadyExistsException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(alreadyExistsException.getMessage());
    }

}
