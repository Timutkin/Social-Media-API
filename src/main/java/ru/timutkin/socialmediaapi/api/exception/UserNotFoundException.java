package ru.timutkin.socialmediaapi.api.exception;

public class UserNotFoundException extends RuntimeException implements NotFoundException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
