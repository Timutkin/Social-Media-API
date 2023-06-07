package ru.timutkin.socialmediaapi.api.service;

import ru.timutkin.socialmediaapi.api.dto.SignupRequest;

public interface RegistrationService {
    void register(SignupRequest request);
}
