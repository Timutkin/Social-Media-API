package ru.timutkin.socialmediaapi.api.exception;

public class UsernameAlreadyExistsException extends RuntimeException implements AlreadyExistsException{
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
