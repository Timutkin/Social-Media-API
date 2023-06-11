package ru.timutkin.socialmediaapi.api.exception;

public class EmailAlreadyExistsException extends RuntimeException implements AlreadyExistsException{
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
