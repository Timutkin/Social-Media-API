package ru.timutkin.socialmediaapi.api.controller;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.timutkin.socialmediaapi.api.constant.ApiConstant;
import ru.timutkin.socialmediaapi.api.mapper.PostMapper;
import ru.timutkin.socialmediaapi.api.security.JwtUtils;
import ru.timutkin.socialmediaapi.storage.repository.FriendRequestRepository;
import ru.timutkin.socialmediaapi.storage.repository.SubscribeRepository;
import ru.timutkin.socialmediaapi.storage.repository.UserRepository;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class FriendshipControllerTest {
    public static final String TESTED_URL = ApiConstant.VERSION_API + "/friends";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private SubscribeRepository subscribeRepository;


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

    }

    @AfterEach
    void tearDown() {
        subscribeRepository.deleteAll();
        friendRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void sendRequestToFriendship_DataIsValid_ShouldReturn200() throws Exception {
        mvc.perform(post(TESTED_URL + "/add/" + friendUserId)
                        .cookie(new Cookie("jwt", jwtCookieMainUser)))
                .andExpect(
                        status().isOk()
                );

    }

    @Test
    void sendRequestToFriendship_FriendRequestAlreadyExistsIs_ShouldReturn409() throws Exception {
        mvc.perform(post(TESTED_URL + "/add/" + friendUserId)
                .cookie(new Cookie("jwt", jwtCookieMainUser))
        );
        mvc.perform(post(TESTED_URL + "/add/" + friendUserId)
                        .cookie(new Cookie("jwt", jwtCookieMainUser)))
                .andExpect(
                        status().isConflict()
                );
    }

    @Test
    void sendRequestToFriendship_FriedRequestToHimSelf_ShouldReturn409() throws Exception {
        mvc.perform(post(TESTED_URL + "/add/" + mainUserId)
                        .cookie(new Cookie("jwt", jwtCookieMainUser)))
                .andExpect(
                        status().isConflict()
                );
    }

    @Test
    void sendRequestToFriendship_UserIsUnauthorized_ShouldReturn409() throws Exception {
        mvc.perform(post(TESTED_URL + "/add/" + mainUserId))
                .andExpect(
                        status().isUnauthorized()
                );
    }

    @Test
    void removeRequestToFriendship_UserIdIsValid_ShouldReturn200() throws Exception {
        mvc.perform(post(TESTED_URL + "/add/" + friendUserId)
                .cookie(new Cookie("jwt", jwtCookieMainUser))
        );
        mvc.perform(delete(TESTED_URL + "/remove/" + friendUserId)
                        .cookie(new Cookie("jwt", jwtCookieMainUser))).
                andExpect(
                        status().isOk()
                );
    }

    @Test
    void removeRequestToFriendship_UserIdIsNonValid_ShouldReturn400() throws Exception {
        mvc.perform(delete(TESTED_URL + "/remove/" + friendUserId)
                        .cookie(new Cookie("jwt", jwtCookieMainUser))).
                andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    void removeRequestToFriendship_UserIsUnauthorized_ShouldReturn401() throws Exception {
        mvc.perform(delete(TESTED_URL + "/remove/" + friendUserId)).
                andExpect(
                        status().isUnauthorized()
                );
    }

    @Test
    void receiveFriendRequest_UserIdISValid_ShouldReturn200() throws Exception {
        mvc.perform(post(TESTED_URL + "/add/" + friendUserId)
                .cookie(new Cookie("jwt", jwtCookieMainUser))
        );
        mvc.perform(post(TESTED_URL + "/receive/" + mainUserId)
                        .cookie(new Cookie("jwt", jwtCookieFriendUser)))
                .andExpect(
                        status().isOk()
                );
    }

    @Test
    void receiveFriendRequest_UserIdIsNonValid_ShouldReturn400() throws Exception {
        mvc.perform(post(TESTED_URL + "/receive/" + mainUserId)
                        .cookie(new Cookie("jwt", jwtCookieFriendUser)))
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    void receiveFriendRequest_UserIdIsNonValid_ShouldReturn409() throws Exception {
        mvc.perform(post(TESTED_URL + "/add/" + friendUserId)
                .cookie(new Cookie("jwt", jwtCookieMainUser))
        );
        mvc.perform(post(TESTED_URL + "/receive/" + mainUserId)
                .cookie(new Cookie("jwt", jwtCookieFriendUser)));
        mvc.perform(post(TESTED_URL + "/receive/" + mainUserId)
                        .cookie(new Cookie("jwt", jwtCookieFriendUser)))
                .andExpect(
                        status().isConflict()
                );
    }

    @Test
    void receiveFriendRequest_UserIdIsNonValid_ShouldReturn401() throws Exception {
        mvc.perform(post(TESTED_URL + "/receive/" + mainUserId))
                .andExpect(
                        status().isUnauthorized()
                );
    }

    @Test
    void rejectFriendRequest_UserIdIsValid_ShouldReturn200() throws Exception {
        mvc.perform(post(TESTED_URL + "/add/" + friendUserId)
                .cookie(new Cookie("jwt", jwtCookieMainUser))
        );
        mvc.perform(post(TESTED_URL + "/reject/" + mainUserId)
                        .cookie(new Cookie("jwt", jwtCookieFriendUser)))
                .andExpectAll(
                        status().isOk()
                );
    }

    @Test
    void rejectFriendRequest_UserIdIsNonValid_ShouldReturn400() throws Exception {
        mvc.perform(post(TESTED_URL + "/reject/" + mainUserId)
                        .cookie(new Cookie("jwt", jwtCookieFriendUser)))
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    void rejectFriendRequest_UserIsUnauthorized_ShouldReturn401() throws Exception {
        mvc.perform(post(TESTED_URL + "/reject/" + mainUserId))
                .andExpect(
                        status().isUnauthorized()
                );
    }

    @Test
    void getAllFromFriendRequests_UserIsAuthorized_ShouldReturn200() throws Exception {
        mvc.perform(post(TESTED_URL + "/add/" + mainUserId)
                .cookie(new Cookie("jwt", jwtCookieFriendUser)));
        mvc.perform(get(TESTED_URL + "/requests/from_users")
                        .cookie(new Cookie("jwt", jwtCookieMainUser))).
                andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].senderId").value(friendUserId.toString())
                );
    }

    @Test
    void getAllFromFriendRequests_UserIsUnauthorized_ShouldReturn401() throws Exception {
        mvc.perform(get(TESTED_URL + "/requests/from_users")).andExpectAll(
                status().isUnauthorized()
        );
    }

    @Test
    void getAllToFriendRequests_UserIsAuthorized_ShouldReturn200() throws Exception {
        mvc.perform(post(TESTED_URL + "/add/" + friendUserId)
                .cookie(new Cookie("jwt", jwtCookieMainUser)));
        mvc.perform(get(TESTED_URL + "/requests/to_users")
                        .cookie(new Cookie("jwt", jwtCookieMainUser))).
                andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].receiverId").value(friendUserId.toString())
                );
    }

    @Test
    void getAllToFriendRequests_UserIsUnauthorized_ShouldReturn401() throws Exception {
        mvc.perform(get(TESTED_URL + "/requests/to_users"))
                .andExpect(
                        status().isUnauthorized()
                );
    }

    @Test
    void getAllFriendsId_UserIsAuthorized_ShouldReturn200() throws Exception {
        mvc.perform(post(TESTED_URL + "/add/" + friendUserId)
                .cookie(new Cookie("jwt", jwtCookieMainUser))
        );
        mvc.perform(post(TESTED_URL + "/receive/" + mainUserId)
                .cookie(new Cookie("jwt", jwtCookieFriendUser)));
        mvc.perform(get(TESTED_URL)
                        .cookie(new Cookie("jwt", jwtCookieMainUser)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0]").value(friendUserId.toString())
                );
    }

    @Test
    void getAllFriendsId_UserIsUnAuthorized_ShouldReturn401() throws Exception {
        mvc.perform(get(TESTED_URL))
                .andExpectAll(
                        status().isUnauthorized()
                );
    }

}