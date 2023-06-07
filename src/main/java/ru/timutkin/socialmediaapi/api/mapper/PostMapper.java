package ru.timutkin.socialmediaapi.api.mapper;

import org.mapstruct.Mapper;
import ru.timutkin.socialmediaapi.api.dto.PostDto;
import ru.timutkin.socialmediaapi.storage.entity.ImageEntity;
import ru.timutkin.socialmediaapi.storage.entity.PostEntity;

@Mapper(componentModel = "spring")
public interface PostMapper {

    default PostDto postEntityToPostDto(PostEntity postEntity){
        return PostDto.builder()
                .id(postEntity.getId())
                .header(postEntity.getHeader())
                .text(postEntity.getText())
                .authorId(postEntity.getAuthor().getId())
                .imagesId(postEntity.getImages().stream().map(ImageEntity::getId).toList())
                .build();
    }
}
