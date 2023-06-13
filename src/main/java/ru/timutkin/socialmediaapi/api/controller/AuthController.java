package ru.timutkin.socialmediaapi.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.timutkin.socialmediaapi.api.constant.ApiConstant;
import ru.timutkin.socialmediaapi.api.dto.LoginRequest;
import ru.timutkin.socialmediaapi.api.dto.SignupRequest;
import ru.timutkin.socialmediaapi.api.dto.UserDto;
import ru.timutkin.socialmediaapi.api.handler.ErrorResponse;
import ru.timutkin.socialmediaapi.api.service.AuthService;
import ru.timutkin.socialmediaapi.api.service.RegistrationService;
import ru.timutkin.socialmediaapi.api.validation.RegistrationValidation;

@Tag(name = "An Auth management APIs ")
@AllArgsConstructor
@RestController
@RequestMapping(
        value = ApiConstant.VERSION_API + "/auth",
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class AuthController {

    private final RegistrationService registrationService;

    private final AuthService authService;


    @Operation(summary = "Registers the user", description = "The username, as well as the password, must be unique." +
                                                             " The password must contain at least one uppercase and lowercase letter, " +
                                                             "one digit and a special character (!@#$%^&*()). " +
                                                             "It should also be between 8 and 20 characters long.",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "The user was successfully registered "
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)),
                            description = "Validation error: the username or email is empty or consists of spaces. " +
                                          "Correct email format : utkin@mail.ru "
                    ),
                    @ApiResponse(responseCode = "409",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)),
                            description = "Username or email already in use"
                    )
            })
    @PostMapping("/signup")
    public ResponseEntity<UserDto> registerUser(
            @RequestBody SignupRequest signupRequest
    ) {
        RegistrationValidation.validate(signupRequest);
        UserDto userDto = registrationService.register(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, authService.authUser(
                        LoginRequest.builder()
                                .username(signupRequest.getUsername())
                                .password(signupRequest.getPassword())
                                .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(userDto);
    }

    @Operation(summary = "Authorizes the user",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The user was successfully authorizes the user "
                    ),
                    @ApiResponse(responseCode = "401",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)),
                            description = "Invalid username or password specified"
                    )
            })
    @PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(
            @RequestBody LoginRequest loginRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, authService.authUser(loginRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .body("User authorized successfully");
    }
}
