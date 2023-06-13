package ru.timutkin.socialmediaapi.api.service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.timutkin.socialmediaapi.api.dto.PostDto;

import java.io.IOException;
import java.util.List;

public interface PostService {
    PostDto savePost(String header, String text, MultipartFile[] multipartFiles,Long userId) throws IOException;

    List<PostDto> findAll(Pageable pageable);

    void deleteById(Long postId, Long userId);

    PostDto updatePost(Long postId, String header, String text, Long userId);

    List<PostDto> findMyPosts(Long userId, Pageable pageable);
}
