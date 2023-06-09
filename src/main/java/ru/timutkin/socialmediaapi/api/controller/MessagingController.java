package ru.timutkin.socialmediaapi.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.timutkin.socialmediaapi.api.constant.ApiConstant;
import ru.timutkin.socialmediaapi.api.handler.ErrorResponse;
import ru.timutkin.socialmediaapi.api.security.service.UserDetailsImpl;
import ru.timutkin.socialmediaapi.api.service.MessagingService;
import ru.timutkin.socialmediaapi.api.validation.Validation;

@Tag(name = "An Messaging management APIs")
@AllArgsConstructor
@RestController
@RequestMapping(
        value = ApiConstant.VERSION_API + "/messages"
)
public class MessagingController {

    private final MessagingService messagingService;

    @Operation(summary = "Sends a messaging request to user with userId",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The messaging request was successfully sent"
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)),
                            description = "The user is trying to send a correspondence request to a non-existent user"
                    ),
                    @ApiResponse(responseCode = "409",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)),
                            description = "The user has already sent a correspondence request to the specified user"
                    ),
                    @ApiResponse(responseCode = "401",
                            content = @Content(schema = @Schema(implementation = HttpServletResponse.class)),
                            description = "User unauthorized"
                    )
            })
    @PostMapping("/add/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendRequestToMessaging(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl user
    ) {
        Validation.validateId(userId);
        messagingService.sendRequestToMessaging(userId, user.getId());
    }

}
