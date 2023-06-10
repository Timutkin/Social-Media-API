package ru.timutkin.socialmediaapi.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.timutkin.socialmediaapi.api.security.service.UserDetailsImpl;
import ru.timutkin.socialmediaapi.api.service.ImageService;
import ru.timutkin.socialmediaapi.api.validation.ImageValidation;
import ru.timutkin.socialmediaapi.api.validation.PostValidation;
import ru.timutkin.socialmediaapi.api.validation.Validation;

import java.io.IOException;

@AllArgsConstructor
@RestController
@RequestMapping(
        value = "/api/images"
)
public class ImageController {

    private final ImageService imageService;


    @PostMapping
    public ResponseEntity<String> addImageToPost(@RequestParam(name = "file") MultipartFile file,
                                                 @RequestParam(name = "post_id") Long postId,
                                                 Authentication principal
    ) throws IOException {
        PostValidation.validateId(postId);
        ImageValidation.validateFile(file);
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        imageService.addImageToPost(file, postId, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body("The image was successfully added to the post");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteImageByPostId(@RequestParam(name = "file") Long fileId,
                                                      @RequestParam(name = "post_id") Long postId,
                                                      Authentication principal
    ) {
        PostValidation.validateId(postId);
        Validation.validateId(fileId);
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        imageService.deleteImageByPostId(fileId, postId, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body("The image was successfully deleted");
    }

    @GetMapping(value = "/{imageId}",
            produces = {
                    MediaType.IMAGE_PNG_VALUE,
                    MediaType.IMAGE_JPEG_VALUE
            })
    public ResponseEntity<byte[]> getImageById(@PathVariable Long imageId
    ) {
        Validation.validateId(imageId);
        byte[] image = imageService.getImageById(imageId);
        return ResponseEntity.ok()
                .body(image);
    }
}
