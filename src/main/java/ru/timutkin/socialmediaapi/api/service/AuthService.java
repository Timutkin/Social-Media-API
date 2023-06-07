package ru.timutkin.socialmediaapi.api.service;

import ru.timutkin.socialmediaapi.api.dto.LoginRequest;

public interface AuthService {
    String authUser(LoginRequest loginRequest);
}
