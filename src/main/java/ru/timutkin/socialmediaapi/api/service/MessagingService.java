package ru.timutkin.socialmediaapi.api.service;

public interface MessagingService {
    void sendRequestToMessaging(Long toUserId, Long fromUserId);
}
