package ru.timutkin.socialmediaapi.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.timutkin.socialmediaapi.api.dto.FriendRequestDto;
import ru.timutkin.socialmediaapi.storage.entity.FriendRequestEntity;

@Mapper(componentModel = "spring")
public interface FriendRequestMapper {

    @Mapping( target = "friendRequestStatus", source = "friendRequestStatus" )
    FriendRequestDto friendRequestEntityToFriendRequestDto(FriendRequestEntity friendRequestEntity);
}
