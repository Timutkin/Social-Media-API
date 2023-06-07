package ru.timutkin.socialmediaapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SignupRequest {
    private String username;

    private String email;

    private String password;
}
