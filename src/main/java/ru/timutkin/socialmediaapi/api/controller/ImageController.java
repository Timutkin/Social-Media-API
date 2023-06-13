package ru.timutkin.socialmediaapi.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.timutkin.socialmediaapi.api.constant.ApiConstant;
import ru.timutkin.socialmediaapi.api.dto.PostDto;
import ru.timutkin.socialmediaapi.api.handler.ErrorResponse;
import ru.timutkin.socialmediaapi.api.security.service.UserDetailsImpl;
import ru.timutkin.socialmediaapi.api.service.ImageService;
import ru.timutkin.socialmediaapi.api.validation.ImageValidation;
import ru.timutkin.socialmediaapi.api.validation.PostValidation;
import ru.timutkin.socialmediaapi.api.validation.Validation;

import java.io.IOException;

@Tag(name = "A image management APIs")
@AllArgsConstructor
@RestController
@RequestMapping(
        value = ApiConstant.VERSION_API + "/images"
)
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "Adds an image to the post",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = PostDto.class))
                            ),
                            description = "The image was successfully added"
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)),
                            description = "Validation error: id is less than or equal to zero. Either the post is not found, " +
                                          "or it does not belong to an authorized user." +
                                          "The transmitted image should not be empty," +
                                          " should be in the format : png, jpg, jpeg."
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "User unauthorized"
                    )
            })
    @PostMapping
    public ResponseEntity<String> addImageToPost(@RequestParam(name = "image") MultipartFile file,
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

    @Operation(summary = "Deletes an image from a post by image id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = PostDto.class))
                            ),
                            description = "The image was successfully deleted"
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)),
                            description = "Validation error: id is less than or equal to zero. Either the post is not found, " +
                                          "or it does not belong to an authorized user or image not found"
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "User unauthorized"
                    )
            })
    @DeleteMapping
    public ResponseEntity<String> deleteImageByPostId(@RequestParam(name = "image_id") Long fileId,
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

    @Operation(summary = "Gets an image by image id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = PostDto.class))
                            ),
                            description = "The image was successfully received"
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)),
                            description = "Validation error: id is less than or equal to zero or image not found"
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "User unauthorized"
                    )
            })
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
