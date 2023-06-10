package ru.timutkin.socialmediaapi.api.exception;

public class FriendRequestNotFoundException extends RuntimeException implements NotFoundException{
    public FriendRequestNotFoundException(String message) {
        super(message);
    }
}
