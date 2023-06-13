package ru.timutkin.socialmediaapi.api.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.timutkin.socialmediaapi.api.dto.PostDto;
import ru.timutkin.socialmediaapi.api.exception.PostNotFoundException;
import ru.timutkin.socialmediaapi.api.mapper.PostMapper;
import ru.timutkin.socialmediaapi.api.service.PostService;
import ru.timutkin.socialmediaapi.storage.entity.ImageEntity;
import ru.timutkin.socialmediaapi.storage.entity.PostEntity;
import ru.timutkin.socialmediaapi.storage.repository.PostRepository;
import ru.timutkin.socialmediaapi.storage.repository.UserRepository;


import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final PostMapper postMapper;

    @Override
    @Transactional
    public PostDto savePost(String header, String text, MultipartFile[] multipartFiles, Long userId) throws IOException {
        PostEntity post = PostEntity.builder()
                .author(userRepository.getById(userId))
                .header(header)
                .text(text)
                .images(new HashSet<>())
                .build();
        if (multipartFiles != null) {
            for (MultipartFile file : multipartFiles) {
                post.addImageEntity(ImageEntity.builder()
                        .name(file.getOriginalFilename())
                        .image(file.getBytes())
                        .build());
            }
        }
        return postMapper.postEntityToPostDto(postRepository.saveAndFlush(post));
    }

    @Override
    @Transactional
    public List<PostDto> findAll(Pageable pageable) {
        return postRepository.findAllFetch(pageable)
                .stream()
                .map(postMapper::postEntityToPostDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long postId, Long userId) {
        if (!postRepository.existsByAuthorIdAndId(userId, postId)) {
            throw new PostNotFoundException("Post with id = " + postId + " not found");
        }
        postRepository.deleteById(postId);
    }

    @Override
    @Transactional
    public PostDto updatePost(Long postId, String header, String text, Long userId) {
        PostEntity post = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException("Post with id = " + postId + " not found")
        );
        if (!Objects.equals(post.getAuthor().getId(), userId)) {
            throw new PostNotFoundException("Post with id = " + postId + " not found");
        }
        post.setText(text != null ? text : post.getText());
        post.setHeader(header != null ? header : post.getHeader());
        return postMapper.postEntityToPostDto(postRepository.saveAndFlush(post));
    }

    @Override
    @Transactional
    public List<PostDto> findMyPosts(Long userId, Pageable pageable) {
        return postRepository.findAllByIdFetch(userId, pageable)
                .stream()
                .map(postMapper::postEntityToPostDto)
                .toList();
    }

}
