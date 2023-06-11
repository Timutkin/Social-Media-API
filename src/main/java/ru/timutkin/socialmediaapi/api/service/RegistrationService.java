package ru.timutkin.socialmediaapi.api.service;

import ru.timutkin.socialmediaapi.api.dto.SignupRequest;
import ru.timutkin.socialmediaapi.api.dto.UserDto;

public interface RegistrationService {
    UserDto register(SignupRequest request);
}
