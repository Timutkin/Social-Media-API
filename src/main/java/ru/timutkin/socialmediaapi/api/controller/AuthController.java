package ru.timutkin.socialmediaapi.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.timutkin.socialmediaapi.api.dto.LoginRequest;
import ru.timutkin.socialmediaapi.api.dto.SignupRequest;
import ru.timutkin.socialmediaapi.api.service.AuthService;
import ru.timutkin.socialmediaapi.api.service.RegistrationService;
import ru.timutkin.socialmediaapi.api.validation.RegistrationValidation;

@AllArgsConstructor
@RestController
@RequestMapping(
        value = "/api/auth",
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class AuthController {

    private final RegistrationService registrationService;

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupRequest signupRequest){
        RegistrationValidation.validate(signupRequest);
        registrationService.register(signupRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body("User registered successfully!");
    }

    @PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE,  authService.authUser(loginRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .body("User authorized successfully");
    }
}
