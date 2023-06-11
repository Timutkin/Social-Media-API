package ru.timutkin.socialmediaapi.api.mapper;

import org.mapstruct.Mapper;
import ru.timutkin.socialmediaapi.api.dto.UserDto;
import ru.timutkin.socialmediaapi.storage.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userEntityToUserDto(UserEntity user);

}
