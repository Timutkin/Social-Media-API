package ru.timutkin.socialmediaapi.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    void addImageToPost(MultipartFile file, Long postId, Long userId) throws IOException;

    void deleteImageByPostId(Long fileId, Long postId, Long userId);

    byte[] getImageById(Long imageId);
}
