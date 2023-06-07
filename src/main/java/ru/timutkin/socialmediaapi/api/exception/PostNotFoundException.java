package ru.timutkin.socialmediaapi.api.exception;

public class PostNotFoundException extends RuntimeException implements NotFoundException{
    public PostNotFoundException(String message) {
        super(message);
    }
}
