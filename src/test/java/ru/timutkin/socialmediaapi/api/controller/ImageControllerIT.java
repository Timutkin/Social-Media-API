package ru.timutkin.socialmediaapi.api.controller;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.timutkin.socialmediaapi.api.constant.ApiConstant;
import ru.timutkin.socialmediaapi.api.security.JwtUtils;
import ru.timutkin.socialmediaapi.storage.repository.ImageRepository;
import ru.timutkin.socialmediaapi.storage.repository.PostRepository;
import ru.timutkin.socialmediaapi.storage.repository.UserRepository;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ImageControllerIT {


    private static final String TESTED_URL = ApiConstant.VERSION_API + "/images";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JwtUtils jwtUtils;
    
    private String jwtCookie;
    private Long authUserId;
    @Autowired
    private ImageRepository imageRepository;

    @BeforeEach
    void setUp() throws Exception {
        jwtCookie = Objects.requireNonNull(mvc.perform(post(AuthControllerIT.TESTED_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                                {
                                "username":"user",
                                "email":"test@mail.ru",
                                "password": "MyPassword123!"
                                }
                                """
                )
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getCookie("jwt")).getValue();
        authUserId = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(jwtCookie)).get().getId();
    }

    @AfterEach
    void tearDown() {
        imageRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void addImageToPost_DataIsValid_ShouldReturn200() throws Exception {
        mvc.perform(post(PostControllerIT.TESTED_URL)
                .param("header", "test header")
                .param("text", "test text")
                .cookie(new Cookie("jwt", jwtCookie))
                .accept(MediaType.APPLICATION_JSON)
        );
        Long postId = postRepository.findByAuthorId(authUserId).getId();
        MockMultipartFile file = new MockMultipartFile("image",
                "original.jpg", MediaType.IMAGE_JPEG_VALUE, "{\"image\": \"C:\\Users\\1\\IdeaProjects\\SocialMediaAPI\\src\\test\\resources\\original.jpg\"}".getBytes());
        mvc.perform(multipart(TESTED_URL)
                .file(file)
                .cookie(new Cookie("jwt", jwtCookie))
                .param("post_id", String.valueOf(postId))
        ).andExpect(
                status().isOk()
        );
    }

    @Test
    void addImageToPost_FileIsNonValid_ShouldReturn400() throws Exception {
        mvc.perform(post(PostControllerIT.TESTED_URL)
                .param("header", "test header")
                .param("text", "test text")
                .cookie(new Cookie("jwt", jwtCookie))
                .accept(MediaType.APPLICATION_JSON)
        );
        Long postId = postRepository.findByAuthorId(authUserId).getId();
        MockMultipartFile file = new MockMultipartFile("image", "empty.txt", MediaType.IMAGE_JPEG_VALUE, "{\"C:\\Users\\1\\IdeaProjects\\SocialMediaAPI\\src\\test\\resources\\empty.txt\"}".getBytes());
        mvc.perform(multipart(TESTED_URL)
                .file(file)
                .cookie(new Cookie("jwt", jwtCookie))
                .param("post_id", String.valueOf(postId))
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    void addImageToPost_PostIdIsNonValid_ShouldReturn400() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image",
                "original.jpg", MediaType.IMAGE_JPEG_VALUE, "{\"image\": \"C:\\Users\\1\\IdeaProjects\\SocialMediaAPI\\src\\test\\resources\\original.jpg\"}".getBytes());
        mvc.perform(multipart(TESTED_URL)
                .file(file)
                .cookie(new Cookie("jwt", jwtCookie))
                .param("post_id", String.valueOf(1))
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    void addImageToPost_UserIsUnauthorized_ShouldReturn401() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image",
                "original.jpg", MediaType.IMAGE_JPEG_VALUE, "{\"image\": \"C:\\Users\\1\\IdeaProjects\\SocialMediaAPI\\src\\test\\resources\\original.jpg\"}".getBytes());
        mvc.perform(multipart(TESTED_URL)
                .file(file)
                .param("post_id", String.valueOf(1))
        ).andExpect(
                status().isUnauthorized()
        );
    }

    @Test
    void deleteImageByPostId_ImageIdIsValid_ShouldReturn200() throws Exception {
        mvc.perform(post(PostControllerIT.TESTED_URL)
                .param("header", "test header")
                .param("text", "test text")
                .cookie(new Cookie("jwt", jwtCookie))
                .accept(MediaType.APPLICATION_JSON)
        );
        Long postId = postRepository.findByAuthorId(authUserId).getId();
        MockMultipartFile file = new MockMultipartFile("image", "original.jpg", MediaType.IMAGE_JPEG_VALUE, "{\"image\": \"C:\\Users\\1\\IdeaProjects\\SocialMediaAPI\\src\\test\\resources\\original.jpg\"}".getBytes());
        mvc.perform(multipart(TESTED_URL)
                .file(file)
                .cookie(new Cookie("jwt", jwtCookie))
                .param("post_id", String.valueOf(postId))
        );
        Long imageId = postRepository.findById(postId).get().getImages().stream().toList().get(0).getId();
        mvc.perform(delete(TESTED_URL)
                        .param("image_id", String.valueOf(imageId))
                        .param("post_id", String.valueOf(postId))
                        .cookie(new Cookie("jwt", jwtCookie)))
                .andExpect(
                        status().isOk()
                );
    }

    @Test
    void deleteImageById_ImageIdIsNonValid_ShouldReturn400() throws Exception {
        mvc.perform(post(PostControllerIT.TESTED_URL)
                .param("header", "test header")
                .param("text", "test text")
                .cookie(new Cookie("jwt", jwtCookie))
                .accept(MediaType.APPLICATION_JSON)
        );
        Long postId = postRepository.findByAuthorId(authUserId).getId();
        mvc.perform(delete(TESTED_URL)
                        .param("post_id", String.valueOf(postId))
                        .param("image_id", String.valueOf(1))
                        .cookie(new Cookie("jwt", jwtCookie)))
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    void deleteImageById_PostIdIsNonValid_ShouldReturn400() throws Exception {
        mvc.perform(delete(TESTED_URL)
                        .param("post_id", String.valueOf(1))
                        .param("image_id", String.valueOf(1))
                        .cookie(new Cookie("jwt", jwtCookie)))
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    void deleteImageById_UserIsUnauthorized_ShouldReturn401() throws Exception {
        mvc.perform(delete(TESTED_URL )
                        .param("post_id", String.valueOf(1))
                        .param("image_id", String.valueOf(1))
                )
                .andExpect(
                        status().isUnauthorized()
                );
    }

    @Test
    void getImageById_ImageIdIsValid_ShouldReturn200() throws Exception {
        mvc.perform(post(PostControllerIT.TESTED_URL)
                .param("header", "test header")
                .param("text", "test text")
                .cookie(new Cookie("jwt", jwtCookie))
                .accept(MediaType.APPLICATION_JSON)
        );
        Long authUserId = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(jwtCookie)).get().getId();
        Long postId = postRepository.findByAuthorId(authUserId).getId();
        MockMultipartFile file = new MockMultipartFile("image", "original.jpg", MediaType.IMAGE_JPEG_VALUE, "{\"image\": \"C:\\Users\\1\\IdeaProjects\\SocialMediaAPI\\src\\test\\resources\\original.jpg\"}".getBytes());
        mvc.perform(multipart(TESTED_URL)
                .file(file)
                .cookie(new Cookie("jwt", jwtCookie))
                .param("post_id", String.valueOf(postId))
        );
        Long imageId = postRepository.findById(postId).get().getImages().stream().toList().get(0).getId();
        mvc.perform(get(TESTED_URL + "/" + imageId)
                        .cookie(new Cookie("jwt", jwtCookie)))
                .andExpect(
                        status().isOk()
                );
    }

    @Test
    void getImageById_ImageIdIsNonValid_ShouldReturn400() throws Exception {
        mvc.perform(get(TESTED_URL + "/" + 1)
                        .cookie(new Cookie("jwt", jwtCookie)))
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    void getImageById_UserIsUnauthorized_ShouldReturn401() throws Exception {
        mvc.perform(get(TESTED_URL + "/" + 1))
                .andExpect(
                        status().isUnauthorized()
                );
    }
}