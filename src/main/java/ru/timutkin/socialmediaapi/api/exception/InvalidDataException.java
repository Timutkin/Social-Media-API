package ru.timutkin.socialmediaapi.api.exception;

public class InvalidDataException extends RuntimeException implements ValidationException{
    public InvalidDataException(String message) {
        super(message);
    }
}
