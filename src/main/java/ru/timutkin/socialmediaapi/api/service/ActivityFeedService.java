package ru.timutkin.socialmediaapi.api.service;

import org.springframework.data.domain.Pageable;
import ru.timutkin.socialmediaapi.api.dto.PostDto;

import java.util.List;

public interface ActivityFeedService {
    List<PostDto> getFeedActivity(Pageable pageable, Long userId);
}
