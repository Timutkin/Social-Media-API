package ru.timutkin.socialmediaapi.api.service;

import ru.timutkin.socialmediaapi.api.dto.FriendRequestDto;

import java.util.List;

public interface FriendshipService {
    void sendRequestToFriendShip(Long toId, Long fromId);

    void removeRequestToFriendShip(Long toId, Long fromId);

    List<FriendRequestDto> getAllFriendRequests(Long id);

    void receiveFriendRequest(Long fromUserId, Long toUserid);

    void rejectFriendRequest(Long fromUserId, Long id);

    List<Long> getAllFriends(Long id);
}
