package ru.timutkin.socialmediaapi.api.exception;

public class FriendRequestAlreadyExistsException extends  RuntimeException implements AlreadyExistsException{
    public FriendRequestAlreadyExistsException(String message) {
        super(message);
    }
}
