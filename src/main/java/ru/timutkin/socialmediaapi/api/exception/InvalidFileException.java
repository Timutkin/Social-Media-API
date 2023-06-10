package ru.timutkin.socialmediaapi.api.exception;

public class InvalidFileException extends RuntimeException implements ValidationException {
    public InvalidFileException(String message) {
        super(message);
    }
}
