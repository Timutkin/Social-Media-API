package ru.timutkin.socialmediaapi.api.exception;

public class AuthUserNotFoundException extends RuntimeException implements AuthException{
    public AuthUserNotFoundException(String message) {
        super(message);
    }
}
