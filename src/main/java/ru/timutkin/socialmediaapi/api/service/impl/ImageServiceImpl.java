package ru.timutkin.socialmediaapi.api.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.timutkin.socialmediaapi.api.exception.ImageNotFoundException;
import ru.timutkin.socialmediaapi.api.exception.PostNotFoundException;
import ru.timutkin.socialmediaapi.api.service.ImageService;
import ru.timutkin.socialmediaapi.storage.entity.ImageEntity;
import ru.timutkin.socialmediaapi.storage.entity.PostEntity;
import ru.timutkin.socialmediaapi.storage.repository.ImageRepository;
import ru.timutkin.socialmediaapi.storage.repository.PostRepository;
import ru.timutkin.socialmediaapi.storage.repository.projection.ImageOnlyBytes;

import java.io.IOException;
import java.util.Objects;

@AllArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final PostRepository postRepository;

    private final ImageRepository imageRepository;

    private static final String POST_NOT_FOUND = "Post with id = %d not found";

    @Override
    @Transactional
    public void addImageToPost(MultipartFile file, Long postId, Long userId) throws IOException {
        PostEntity post = postRepository.findByIdWithImagesAndAuthor(postId).orElseThrow(
                () -> new PostNotFoundException(POST_NOT_FOUND.formatted(postId))
        );
        if (!Objects.equals(post.getAuthor().getId(), userId)){
            throw new PostNotFoundException(POST_NOT_FOUND.formatted(postId));
        }
        ImageEntity image = ImageEntity.builder()
                .name(file.getOriginalFilename())
                .image(file.getBytes())
                .build();
        post.addImageEntity(image);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void deleteImageByPostId(Long fileId, Long postId, Long userId) {
        PostEntity post = postRepository.findByIdWithImagesAndAuthor(postId).orElseThrow(
                () -> new PostNotFoundException(POST_NOT_FOUND.formatted(postId))
        );
        if (!Objects.equals(post.getAuthor().getId(), userId)){
            throw new PostNotFoundException(POST_NOT_FOUND.formatted(postId));
        }
        if (post.getImages().stream().map(ImageEntity::getId).toList().contains(fileId)){
            imageRepository.deleteById(fileId);
        }
        else{
            throw new ImageNotFoundException("The image does not match this post");
        }

    }

    @Override
    @Transactional
    public byte[] getImageById(Long imageId) {
        ImageOnlyBytes image = imageRepository.getByImageId(imageId).orElseThrow(
                () ->  new ImageNotFoundException("Image not found")
        );
        return image.getImage();
    }
}
