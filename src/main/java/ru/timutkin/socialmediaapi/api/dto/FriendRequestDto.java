package ru.timutkin.socialmediaapi.api.dto;

import lombok.Data;
import ru.timutkin.socialmediaapi.storage.enumeration.FriendRequestStatus;

@Data
public class FriendRequestDto {
    Long senderId;
    FriendRequestStatus friendRequestStatus;
}
