package ru.timutkin.socialmediaapi.storage.repository.projection;

import ru.timutkin.socialmediaapi.storage.entity.ImageEntity;

import java.util.Date;
import java.util.List;

public interface PostDto {
    Long getId();
    String getUsername();
    String getHeader();
    String getText();
    Date getCreated();
    List<ImageEntity> getImages();


}
