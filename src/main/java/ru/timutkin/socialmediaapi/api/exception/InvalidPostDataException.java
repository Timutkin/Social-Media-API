package ru.timutkin.socialmediaapi.api.exception;

public class InvalidPostDataException extends RuntimeException implements ValidationException{
    public InvalidPostDataException(String message) {
        super(message);
    }
}
