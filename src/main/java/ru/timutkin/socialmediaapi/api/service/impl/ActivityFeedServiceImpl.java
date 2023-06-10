package ru.timutkin.socialmediaapi.api.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.timutkin.socialmediaapi.api.dto.PostDto;
import ru.timutkin.socialmediaapi.api.mapper.PostMapper;
import ru.timutkin.socialmediaapi.api.service.ActivityFeedService;
import ru.timutkin.socialmediaapi.storage.repository.PostRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class ActivityFeedServiceImpl implements ActivityFeedService {

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    @Override
    @Transactional
    public List<PostDto> getFeedActivity(Pageable pageable, Long userId) {
        return postRepository.findBySubscribe(userId, pageable)
                .stream()
                .map(postMapper::postEntityToPostDto)
                .toList();
    }
}
