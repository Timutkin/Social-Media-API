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
import ru.timutkin.socialmediaapi.storage.repository.UserRepository;

import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ActivityFeedControllerTest {

    public static final String TESTED_URL = ApiConstant.VERSION_API + "/activity";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private String jwtCookieMainUser;

    private String jwtCookieFriendUser;

    private Long mainUserId;

    private Long friendUserId;

    @BeforeEach
    void setUp() throws Exception {
        jwtCookieMainUser = Objects.requireNonNull(mvc.perform(post(AuthControllerIT.TESTED_URL + "/signup")
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
        mainUserId = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(jwtCookieMainUser)).get().getId();
        jwtCookieFriendUser = Objects.requireNonNull(mvc.perform(post(AuthControllerIT.TESTED_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                                {
                                "username":"user two",
                                "email":"testtwo@mail.ru",
                                "password": "MyPassword123!"
                                }
                                """
                )
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getCookie("jwt")).getValue();
        friendUserId = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(jwtCookieFriendUser)).get().getId();
        MockMultipartFile file = new MockMultipartFile("images", "original.jpg", MediaType.IMAGE_JPEG_VALUE, "{\"images\": \"C:\\Users\\1\\IdeaProjects\\SocialMediaAPI\\src\\test\\resources\\original.jpg\"}".getBytes());
        mvc.perform(multipart(PostControllerIT.TESTED_URL)
                .file(file)
                .param("header", "test header")
                .param("text", "test text")
                .cookie(new Cookie("jwt", jwtCookieMainUser))
                .accept(MediaType.APPLICATION_JSON)
        );
        mvc.perform(multipart(PostControllerIT.TESTED_URL)
                .file(file)
                .param("header", "test header2")
                .param("text", "test text2")
                .cookie(new Cookie("jwt", jwtCookieMainUser))
                .accept(MediaType.APPLICATION_JSON)
        );
        mvc.perform(multipart(PostControllerIT.TESTED_URL)
                .file(file)
                .param("header", "test header3")
                .param("text", "test text")
                .cookie(new Cookie("jwt", jwtCookieMainUser))
                .accept(MediaType.APPLICATION_JSON)
        );
        mvc.perform(post(FriendshipControllerTest.TESTED_URL + "/add/" + mainUserId)
                .cookie(new Cookie("jwt", jwtCookieFriendUser)));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getFeedActivity_UserIsAuthorized_WithoutParam_ShouldReturn200() throws Exception {
        mvc.perform(get(TESTED_URL)
                        .cookie(new Cookie("jwt", jwtCookieFriendUser))
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.*", hasSize(3))
                );
    }

    @Test
    void getFeedActivity_UserIsAuthorized_Wi_ShouldReturn200() throws Exception {
        mvc.perform(get(TESTED_URL)
                        .cookie(new Cookie("jwt", jwtCookieFriendUser))
                        .param("size", "1")
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.*", hasSize(1))
                );
    }

    @Test
    void getFeedActivity_UserIsUnauthorized_ShouldReturn401() throws Exception {
        mvc.perform(get(TESTED_URL))
                .andExpect(
                        status().isUnauthorized()
                );
    }
}