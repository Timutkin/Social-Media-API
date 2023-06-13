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

import static ru.timutkin.socialmediaapi.api.mapper.PostMapper.IMAGE_RESOURCE;

@AllArgsConstructor
@Service
public class ActivityFeedServiceImpl implements ActivityFeedService {

    private final PostRepository postRepository;


    @Override
    @Transactional
    public List<PostDto> getFeedActivity(Pageable pageable, Long userId) {
        return postRepository.findBySubscribe(userId, pageable)
                .stream()
                .map(postDto -> PostDto.builder()
                        .id(postDto.getId())
                        .text(postDto.getText())
                        .header(postDto.getHeader())
                        .authorUsername(postDto.getUsername())
                        .created(postDto.getCreated())
                        .images(
                                postDto.getImages().stream()
                                        .map(img-> IMAGE_RESOURCE + img.getId())
                                        .toList()
                        ).build())
                .toList();
    }
}
