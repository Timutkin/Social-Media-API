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
import ru.timutkin.socialmediaapi.api.dto.PostDto;
import ru.timutkin.socialmediaapi.api.mapper.PostMapper;
import ru.timutkin.socialmediaapi.api.security.JwtUtils;
import ru.timutkin.socialmediaapi.storage.entity.PostEntity;
import ru.timutkin.socialmediaapi.storage.repository.ImageRepository;
import ru.timutkin.socialmediaapi.storage.repository.PostRepository;
import ru.timutkin.socialmediaapi.storage.repository.UserRepository;

import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class PostControllerIT {

    public static final String TESTED_URL = ApiConstant.VERSION_API + "/posts";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JsonConverter jsonConverter;

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
    void downUp() {
        imageRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createPost_DataIsValid_ShouldReturn201() throws Exception {
        MockMultipartFile file = new MockMultipartFile("images", "original.jpg", MediaType.IMAGE_JPEG_VALUE, "{\"images\": \"C:\\Users\\1\\IdeaProjects\\SocialMediaAPI\\src\\test\\resources\\original.jpg\"}".getBytes());
        mvc.perform(multipart(TESTED_URL)
                        .file(file)
                        .param("header", "test header")
                        .param("text", "test text")
                        .cookie(new Cookie("jwt", jwtCookie))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void createPost_HeaderIsNonValid_ShouldReturn400() throws Exception {
        mvc.perform(post(TESTED_URL)
                        .param("header", " ")
                        .param("text", "test text")
                        .cookie(new Cookie("jwt", jwtCookie))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPost_TextIsNonValid_ShouldReturn400() throws Exception {
        mvc.perform(post(TESTED_URL)
                        .param("header", "header")
                        .param("text", "  ")
                        .cookie(new Cookie("jwt", jwtCookie))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPost_UserIsUnauthorized_ShouldReturn401() throws Exception {
        mvc.perform(post(TESTED_URL)
                        .param("header", "header")
                        .param("text", "text")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createPost_ImageIsNonValid_ShouldReturn400() throws Exception {
        MockMultipartFile file = new MockMultipartFile("images", "empty.txt", MediaType.IMAGE_JPEG_VALUE, "{\"C:\\Users\\1\\IdeaProjects\\SocialMediaAPI\\src\\test\\resources\\empty.txt\"}".getBytes());
        mvc.perform(multipart(TESTED_URL)
                        .file(file)
                        .param("header", "test header")
                        .param("text", "test text")
                        .cookie(new Cookie("jwt", jwtCookie))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }


    @Test
    void findAll_ShouldReturn200() throws Exception {
        postRepository.save(PostEntity.builder()
                .header("header1")
                .text("text1")
                .build());
        postRepository.save(PostEntity.builder()
                .header("header2")
                .text("text2")
                .build());
        List<PostDto> postDtos = postRepository.findAll().stream().map(postMapper::postEntityToPostDto).toList();
        mvc.perform(get(TESTED_URL)
                        .cookie(new Cookie("jwt", jwtCookie))
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(jsonConverter.convert(postDtos))
                );
    }

    @Test
    void findAll_ShouldReturn401() throws Exception {
        mvc.perform(get(TESTED_URL))
                .andExpect(
                        status().isUnauthorized()
                );
    }

    @Test
    void findMyPosts_shouldReturn200() throws Exception {
        MockMultipartFile file = new MockMultipartFile("images", "original.jpg", MediaType.IMAGE_JPEG_VALUE, "{\"images\": \"C:\\Users\\1\\IdeaProjects\\SocialMediaAPI\\src\\test\\resources\\original.jpg\"}".getBytes());
        mvc.perform(multipart(TESTED_URL)
                .file(file)
                .param("header", "test header")
                .param("text", "test text")
                .cookie(new Cookie("jwt", jwtCookie))
                .accept(MediaType.APPLICATION_JSON)
        );
        mvc.perform(post(TESTED_URL)
                .param("header", "header")
                .param("text", "text")
                .cookie(new Cookie("jwt", jwtCookie))
                .accept(MediaType.APPLICATION_JSON)
        );
        List<PostDto> postDtos = postRepository.findAllByIdFetch(authUserId).stream().map(postMapper::postEntityToPostDto).toList();
        mvc.perform(get(TESTED_URL + "/my")
                        .cookie(new Cookie("jwt", jwtCookie))
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(jsonConverter.convert(postDtos))
                );
    }

    @Test
    void findMyPosts_UserIsUnauthorized_ShouldReturn401() throws Exception {
        mvc.perform(get(TESTED_URL))
                .andExpect(
                        status().isUnauthorized()
                );
    }


    @Test
    void deletePostById_PostIdIsValid_ShouldReturn200() throws Exception {
        mvc.perform(post(TESTED_URL)
                .param("header", "header")
                .param("text", "text")
                .cookie(new Cookie("jwt", jwtCookie))
                .accept(MediaType.APPLICATION_JSON)
        );
        Long postId = postRepository.findByAuthorId(authUserId).getId();
        mvc.perform(delete(TESTED_URL + "/" + postId)
                        .cookie(new Cookie("jwt", jwtCookie))
                )
                .andExpectAll(
                        status().isOk(),
                        content().json(String.valueOf(postId))
                );
    }

    @Test
    void deletePostById_PostIdIsNonValid_ShouldReturn400() throws Exception {
        mvc.perform(delete(TESTED_URL + "/1")
                        .cookie(new Cookie("jwt", jwtCookie))
                )
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    void deletePostById_UserIsUnauthorized_ShouldReturn400() throws Exception {
        mvc.perform(delete(TESTED_URL + "/1")
                )
                .andExpect(
                        status().isUnauthorized()
                );
    }

    @Test
    void updatePost_DataIsValid_ShouldReturn200() throws Exception {
        mvc.perform(post(TESTED_URL)
                .param("header", "header")
                .param("text", "text")
                .cookie(new Cookie("jwt", jwtCookie))
                .accept(MediaType.APPLICATION_JSON)
        );
        PostEntity post = postRepository.findByAuthorId(authUserId);
        PostDto updatedPostDto = postMapper.postEntityToPostDto(post);
        updatedPostDto.setText("new text");
        updatedPostDto.setHeader("new header");
        mvc.perform(patch(TESTED_URL)
                .param("header", "new header")
                .param("text", "new text")
                .param("post_id", String.valueOf(post.getId()))
                .cookie(new Cookie("jwt", jwtCookie))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json(jsonConverter.convert(updatedPostDto))
        );
    }

    @Test
    void updatePost_HeaderIsNonValid_ShouldReturn400() throws Exception {
        mvc.perform(post(TESTED_URL)
                .param("header", "header")
                .param("text", "text")
                .cookie(new Cookie("jwt", jwtCookie))
                .accept(MediaType.APPLICATION_JSON)
        );
        Long  postId = postRepository.findByAuthorId(authUserId).getId();
        mvc.perform(patch(TESTED_URL)
                .param("header", "  ")
                .param("post_id", String.valueOf(postId))
                .cookie(new Cookie("jwt", jwtCookie))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    void updatePost_TextIsNonValid_ShouldReturn400() throws Exception {
        mvc.perform(post(TESTED_URL)
                .param("header", "header")
                .param("text", "text")
                .cookie(new Cookie("jwt", jwtCookie))
                .accept(MediaType.APPLICATION_JSON)
        );
        Long  postId = postRepository.findByAuthorId(authUserId).getId();
        mvc.perform(patch(TESTED_URL)
                .param("text", "  ")
                .param("post_id", String.valueOf(postId))
                .cookie(new Cookie("jwt", jwtCookie))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    void updatePost_PostIdIsNonValid_ShouldReturn400() throws Exception {
        mvc.perform(patch(TESTED_URL)
                .param("header", "header")
                .param("text", "text")
                .param("post_id", String.valueOf(1))
                .cookie(new Cookie("jwt", jwtCookie))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    void updatePost_UserIsUnauthorized_ShouldReturn401() throws Exception {
        mvc.perform(patch(TESTED_URL)
                .param("header", "header")
                .param("text", "text")
                .param("post_id", String.valueOf(1))
        ).andExpect(
                status().isUnauthorized()
        );
    }
}