package ru.timutkin.socialmediaapi.api.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.timutkin.socialmediaapi.api.exception.*;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ InternalAuthenticationServiceException.class, BadCredentialsException.class })
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Authentication failed : the username or password is incorrect "));
    }

    @ExceptionHandler(value = {
            InvalidFileException.class,
            InvalidRegistrationDataException.class,
            InvalidPostDataException.class})
    protected ResponseEntity<ErrorResponse> handleValidationException(ValidationException validationException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(validationException.getMessage()));
    }

    @ExceptionHandler(value = {
            PostNotFoundException.class,
            FriendRequestNotFoundException.class,
            UserNotFoundException.class,
            ImageNotFoundException.class})
    protected ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException notFoundException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(notFoundException.getMessage()));
    }

    @ExceptionHandler(value = {FriendRequestAlreadyExistsException.class,
            MessageRequestAlreadyExistsException.class,
            UsernameAlreadyExistsException.class,
            EmailAlreadyExistsException.class
    })
    protected ResponseEntity<ErrorResponse> handleAlreadyExistsException(AlreadyExistsException alreadyExistsException) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(alreadyExistsException.getMessage()));
    }


}
