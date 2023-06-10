package ru.timutkin.socialmediaapi.api.exception;

public class ImageNotFoundException extends RuntimeException implements NotFoundException{
    public ImageNotFoundException(String message) {
        super(message);
    }
}
