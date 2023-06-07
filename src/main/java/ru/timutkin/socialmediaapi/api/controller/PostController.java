package ru.timutkin.socialmediaapi.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.timutkin.socialmediaapi.api.dto.PostDto;
import ru.timutkin.socialmediaapi.api.security.service.UserDetailsImpl;
import ru.timutkin.socialmediaapi.api.service.PostService;
import ru.timutkin.socialmediaapi.api.validation.PostValidation;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(
        value = "/api/posts",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
)
public class PostController {

    private final PostService postService;

    @PostMapping()
    public ResponseEntity<PostDto> createPost(@RequestParam(name = "header") String header,
                                              @RequestParam(name = "text") String text,
                                              @RequestParam(name = "images") MultipartFile[] multipartFiles,
                                              Authentication principal
    ) throws IOException {
        PostValidation.validatePostCreation(header, text, multipartFiles);
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        PostDto postDto = postService.savePost(header, text, multipartFiles, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(postDto);
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> findAll(){
        List<PostDto> posts = postService.findAll();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(posts);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Long> deletePostById(@PathVariable Long postId,
                                               Authentication principal
                                               ){
        PostValidation.validateId(postId);
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        postService.deleteById(postId, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(postId);
    }

    @PatchMapping()
    public ResponseEntity<PostDto> updatePost(@RequestParam(name = "post_id") Long postId,
                                              @RequestParam(name = "header", required = false) String header,
                                              @RequestParam(name = "text", required = false) String text,
                                              Authentication principal
                                              ){
        PostValidation.validateHeaderAndTextUpdate(header,text);
        PostValidation.validateId(postId);
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        PostDto postDto = postService.updatePost(postId, header, text, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(postDto);
    }



}
