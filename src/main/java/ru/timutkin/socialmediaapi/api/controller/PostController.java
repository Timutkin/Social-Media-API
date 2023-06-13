package ru.timutkin.socialmediaapi.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
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
import ru.timutkin.socialmediaapi.api.service.PostService;
import ru.timutkin.socialmediaapi.api.validation.PostValidation;

import java.io.IOException;
import java.util.List;

@Tag(name = "A Post management APIs")
@AllArgsConstructor
@RestController
@RequestMapping(
        value = ApiConstant.VERSION_API + "/posts"
)
public class PostController {

    private final PostService postService;

    @Operation(summary = "Creates a post",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "The post was successfully created "
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)),
                            description = "Validation error: the text or header is empty or consists of spaces." +
                                          "The transmitted image, the images should not be empty, " +
                                          "should be in the format : png, jpg, jpeg. " +
                                          "Also , there should not be more than three photos ."
                    ),
                    @ApiResponse(responseCode = "401",
                            content = @Content(schema = @Schema(implementation = HttpServletResponse.class)),
                            description = "User unauthorized"

                    )
            })
    @PostMapping()
    public ResponseEntity<PostDto> createPost(
            @RequestParam(name = "header") String header,
            @RequestParam(name = "text") String text,
            @RequestParam(name = "images", required = false) MultipartFile[] multipartFiles,
            Authentication principal
    ) throws IOException {
        PostValidation.validatePostCreation(header, text, multipartFiles);
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        PostDto postDto = postService.savePost(header, text, multipartFiles, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(postDto);
    }

    @Operation(summary = "Gets a all posts of users order by created desc",
            description = """
                    page: the index of page that we want to retrieve – the parameter is zero-indexed and its default value is 0
                    size: the number of pages that we want to retrieve – the default value is 20
                    sort: one or more properties that we can use for sorting the results, using the following format: 
                    property1,property2(,asc|desc) – for instance, ?sort=name&sort=email,asc
                    """,
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = PostDto.class))
                            ),
                            description = "The posts was successfully received"
                    ),
                    @ApiResponse(responseCode = "401",
                            content = @Content(schema = @Schema(implementation = HttpServletResponse.class)),
                            description = "User unauthorized"
                    )
            })
    @GetMapping
    public ResponseEntity<List<PostDto>> findAll(
            Pageable pageable
    ) {
        List<PostDto> posts = postService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(posts);
    }


    @Operation(summary = "Gets a all posts of auth user order by created desc",
            description = """
                    page: the index of page that we want to retrieve – the parameter is zero-indexed and its default value is 0
                    size: the number of pages that we want to retrieve – the default value is 20
                    sort: one or more properties that we can use for sorting the results, using the following format: 
                    property1,property2(,asc|desc) – for instance, ?sort=name&sort=email,asc
                    """,
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = PostDto.class))
                            ),
                            description = "The posts was successfully received"
                    ),
                    @ApiResponse(responseCode = "401",
                            content = @Content(schema = @Schema(implementation = HttpServletResponse.class)),
                            description = "User unauthorized"
                    )
            })
    @GetMapping("/my")
    public ResponseEntity<List<PostDto>> findMyPosts(
            Authentication principal,
            Pageable pageable
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        List<PostDto> posts = postService.findMyPosts(userDetails.getId(), pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(posts);
    }

    @Operation(summary = "Deletes a post by post_id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = PostDto.class))
                            ),
                            description = "The post was successfully deleted"
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)),
                            description = "Validation error: id is less than or equal to zero. " +
                                          "Either the post is not found, " +
                                          "or it does not belong to an authorized user"
                    ),
                    @ApiResponse(responseCode = "401",
                            content = @Content(schema = @Schema(implementation = HttpServletResponse.class)),
                            description = "User unauthorized"
                    )
            })
    @DeleteMapping("/{post_id}")
    public ResponseEntity<Long> deletePostById(
            @PathVariable Long post_id,
            Authentication principal
    ) {
        PostValidation.validateId(post_id);
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        postService.deleteById(post_id, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(post_id);
    }

    @Operation(summary = "Updates a post by post_id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = PostDto.class))
                            ),
                            description = "The post was successfully updated"
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)),
                            description = "Validation error: the text or header consists of spaces or " +
                                          "post id is less than or equal to zero.Either the post is not found, " +
                                          "or it does not belong to an authorized user "

                    ),
                    @ApiResponse(responseCode = "401",
                            content = @Content(schema = @Schema(implementation = HttpServletResponse.class)),
                            description = "User unauthorized"
                    )
            })
    @PatchMapping()
    public ResponseEntity<PostDto> updatePost(
            @RequestParam(name = "post_id") Long postId,
            @RequestParam(name = "header", required = false) String header,
            @RequestParam(name = "text", required = false) String text,
            Authentication principal
    ) {
        PostValidation.validateHeaderAndTextUpdate(header, text);
        PostValidation.validateId(postId);
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        PostDto postDto = postService.updatePost(postId, header, text, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(postDto);
    }


}
