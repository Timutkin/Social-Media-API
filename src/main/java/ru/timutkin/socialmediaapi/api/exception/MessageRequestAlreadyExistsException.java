package ru.timutkin.socialmediaapi.api.exception;

public class MessageRequestAlreadyExistsException extends RuntimeException implements AlreadyExistsException{
    public MessageRequestAlreadyExistsException(String message) {
        super(message);
    }
}
