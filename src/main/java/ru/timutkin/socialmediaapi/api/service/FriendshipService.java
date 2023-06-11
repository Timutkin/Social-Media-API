package ru.timutkin.socialmediaapi.api.service;

import ru.timutkin.socialmediaapi.api.dto.FriendFromRequestDto;
import ru.timutkin.socialmediaapi.api.dto.FriendToRequestDto;

import java.util.List;

public interface FriendshipService {
    void sendRequestToFriendShip(Long toId, Long fromId);

    void removeRequestToFriendShip(Long toId, Long fromId);

    List<FriendFromRequestDto> getAllFromFriendRequests(Long id);

    void receiveFriendRequest(Long fromUserId, Long toUserid);

    void rejectFriendRequest(Long fromUserId, Long id);

    List<Long> getAllFriends(Long id);

    List<FriendToRequestDto> getAllToFriendRequests(Long id);
}
