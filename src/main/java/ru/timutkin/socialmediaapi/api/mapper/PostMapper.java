package ru.timutkin.socialmediaapi.api.mapper;

import org.mapstruct.Mapper;
import ru.timutkin.socialmediaapi.api.dto.PostDto;
import ru.timutkin.socialmediaapi.storage.entity.PostEntity;

@Mapper(componentModel = "spring")
public interface PostMapper {

    String IMAGE_RESOURCE = "http://localhost:2000/api/v1/images/";

    default PostDto postEntityToPostDto(PostEntity postEntity){
        return PostDto.builder()
                .id(postEntity.getId())
                .created(postEntity.getCreated())
                .header(postEntity.getHeader())
                .authorUsername(postEntity.getAuthor().getUsername())
                .text(postEntity.getText())
                .images(postEntity.getImages().stream()
                        .map(img-> IMAGE_RESOURCE + img.getId())
                        .toList())
                .build();
    }
}
