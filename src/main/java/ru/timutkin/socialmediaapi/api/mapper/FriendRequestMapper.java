package ru.timutkin.socialmediaapi.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.timutkin.socialmediaapi.api.dto.FriendFromRequestDto;
import ru.timutkin.socialmediaapi.api.dto.FriendToRequestDto;
import ru.timutkin.socialmediaapi.storage.entity.FriendRequestEntity;

@Mapper(componentModel = "spring")
public interface FriendRequestMapper {

    @Mapping( target = "friendRequestStatus", source = "friendRequestStatus" )
    FriendFromRequestDto friendRequestEntityToFriendFromRequestDto(FriendRequestEntity friendRequestEntity);
    @Mapping( target = "friendRequestStatus", source = "friendRequestStatus" )
    FriendToRequestDto friendRequestEntityToFriendToRequestDto(FriendRequestEntity friendRequestEntity);
}
