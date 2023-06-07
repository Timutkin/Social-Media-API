package ru.timutkin.socialmediaapi.api.exception;

public class InvalidRegistrationDataException extends RuntimeException implements ValidationException{
    public InvalidRegistrationDataException(String message) {
        super(message);
    }
}
