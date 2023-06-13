package ru.timutkin.socialmediaapi.api.controller;


import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.timutkin.socialmediaapi.api.constant.ApiConstant;
import ru.timutkin.socialmediaapi.storage.repository.UserRepository;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIT {

    public static final String TESTED_URL = ApiConstant.VERSION_API + "/auth";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void downUp(){
        userRepository.deleteAll();
    }

    @Test
    void registerUser_BodyIsValid_ShouldReturn201() throws Exception {
        mvc.perform(post(TESTED_URL+ "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                        "username":"testuser",
                        "email":"test@mail.ru",
                        "password": "MyPassword123!"
                        }
                        """
                )
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.username").value("testuser"),
                        jsonPath("$.email").value("test@mail.ru")
                );
    }
    @Test
    void registerUser_UsernameIsNonValid_ShouldReturn400() throws Exception {
        mvc.perform(post(TESTED_URL+ "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                        "username":"   ",
                        "email":"test@mail.ru",
                        "password": "MyPassword123!"
                        }
                        """
                )
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.errorMessage", startsWith("The user name should not be empty,"))
        );
    }

    @Test
    void registerUser_EmailIsNonValid_ShouldReturn400() throws Exception {
        mvc.perform(post(TESTED_URL+ "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                        "username":"user",
                        "email":"testmail.ru",
                        "password": "MyPassword123!"
                        }
                        """
                )
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.errorMessage", startsWith("Incorrect email format"))
        );
    }

    @Test
    void registerUser_PasswordIsNonValid_ShouldReturn400() throws Exception {
        mvc.perform(post(TESTED_URL+ "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                        "username":"user",
                        "email":"test@mail.ru",
                        "password": "MyPassword123"
                        }
                        """
                )
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.errorMessage", startsWith("The password is incorrect"))
        );
    }

    @Test
    void registerUser_UsernameAlreadyInUse_ShouldReturn409() throws Exception {
        mvc.perform(post(TESTED_URL+ "/signup")
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
                        .accept(MediaType.APPLICATION_JSON));
        mvc.perform(post(TESTED_URL+ "/signup")
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
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isConflict(),
                jsonPath("$.errorMessage", startsWith("Username is already taken!"))
        );
    }

    @Test
    void registerUser_EmailAlreadyInUse_ShouldReturn409() throws Exception {
        mvc.perform(post(TESTED_URL+ "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                        "username":"user2",
                        "email":"test@mail.ru",
                        "password": "MyPassword123!"
                        }
                        """
                )
                .accept(MediaType.APPLICATION_JSON));
        mvc.perform(post(TESTED_URL+ "/signup")
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
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isConflict(),
                jsonPath("$.errorMessage", startsWith("Email is already in use!"))
        );
    }

    @Test
    void authenticateUser_ValidAuthData_ShouldReturn200() throws Exception {
        mvc.perform(post(TESTED_URL+ "/signup")
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
                .accept(MediaType.APPLICATION_JSON));
        mvc.perform(post(TESTED_URL+ "/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                        "username":"user",
                        "password": "MyPassword123!"
                        }
                        """
                )
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk());
    }

    @Test
    void authenticateUser_InValidAuthData_ShouldReturn401() throws Exception {
        mvc.perform(post(TESTED_URL+ "/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                        "username":"user",
                        "password": "MyPassword123!"
                        }
                        """
                )
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }
}