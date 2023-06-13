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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.timutkin.socialmediaapi.api.constant.ApiConstant;
import ru.timutkin.socialmediaapi.api.dto.FriendFromRequestDto;
import ru.timutkin.socialmediaapi.api.dto.FriendToRequestDto;
import ru.timutkin.socialmediaapi.api.handler.ErrorResponse;
import ru.timutkin.socialmediaapi.api.security.service.UserDetailsImpl;
import ru.timutkin.socialmediaapi.api.service.FriendshipService;
import ru.timutkin.socialmediaapi.api.validation.Validation;

import java.util.List;

@Tag(name = "An Friendship management APIs")
@AllArgsConstructor
@RestController
@RequestMapping(
        value = ApiConstant.VERSION_API + "/friends"
)
public class FriendshipController {

    private final FriendshipService friendshipService;

    @Operation(summary = "Sends a friend request to user with userId",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The friend request was successfully sent"
                    ),
                    @ApiResponse(responseCode = "409",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)),
                            description = "The user is trying to send an application to himself,or " +
                                          "you have already accepted the application as a friend"
                    ),
                    @ApiResponse(responseCode = "401",
                            content = @Content(schema = @Schema(implementation = HttpServletResponse.class)),
                            description = "User unauthorized"
                    )
            })
    @PostMapping("/add/{userId}")
    public ResponseEntity<String> sendRequestToFriendship(
            @PathVariable Long userId,
            Authentication principal
    ) {
        Validation.validateId(userId);
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        friendshipService.sendRequestToFriendShip(userId, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body("The friend request has been sent successfully");
    }

    @Operation(summary = "Removes a friend request to user with userId",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The friend request was successfully removed"
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)),
                            description = "The application to the specified user was not found"
                    ),
                    @ApiResponse(responseCode = "401",
                            content = @Content(schema = @Schema(implementation = HttpServletResponse.class)),
                            description = "User unauthorized"
                    )
            })
    @DeleteMapping("/remove/{userId}")
    public ResponseEntity<String> removeRequestToFriendship(
            @PathVariable Long userId,
            Authentication principal
    ) {
        Validation.validateId(userId);
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        friendshipService.removeRequestToFriendShip(userId, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body("The friend request has been removed successfully");
    }

    @Operation(summary = "Receives a friend request to user with userId",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The friend request was successfully removed"
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)),
                            description = "The application from the specified user was not found "
                    ),
                    @ApiResponse(responseCode = "409",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)),
                            description = "The user has already been added to friends"
                    ),
                    @ApiResponse(responseCode = "401",
                            content = @Content(schema = @Schema(implementation = HttpServletResponse.class)),
                            description = "User unauthorized"
                    )
            })
    @PostMapping("/receive/{userId}")
    public ResponseEntity<String> receiveFriendRequest(
            @PathVariable Long userId,
            Authentication principal
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        friendshipService.receiveFriendRequest(userId, userDetails.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("The user has been successfully added to friends");
    }

    @Operation(summary = "Rejects a friend request to user with userId", description = """
            If the users have not been friends before, then the user rejects the request
            If the users were previously friends, then the user is removed from friends
            """,
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The friend request was successfully rejected"
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)),
                            description = "The friend request from the specified user was not found"
                    ),
                    @ApiResponse(responseCode = "401",
                            content = @Content(schema = @Schema(implementation = HttpServletResponse.class)),
                            description = "User unauthorized"
                    )
            })
    @PostMapping("/reject/{fromUserId}")
    public ResponseEntity<String> rejectFriendRequest(
            @PathVariable Long fromUserId,
            Authentication principal
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        friendshipService.rejectFriendRequest(fromUserId, userDetails.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("The user has been successfully rejected to friends");
    }

    @Operation(summary = "Gets all friends requests FROM another users",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The friend requests was successfully received"
                    ),
                    @ApiResponse(responseCode = "401",
                            content = @Content(schema = @Schema(implementation = HttpServletResponse.class)),
                            description = "User unauthorized"
                    )
            })
    @GetMapping("/requests/from_users")
    public ResponseEntity<List<FriendFromRequestDto>> getAllFromFriendRequests(
            Authentication principal
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        List<FriendFromRequestDto> friendFromRequestDtoList = friendshipService.getAllFromFriendRequests(userDetails.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(friendFromRequestDtoList);
    }

    @Operation(summary = "Gets all friends requests TO another users",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The friend requests was successfully received"
                    ),
                    @ApiResponse(responseCode = "401",
                            content = @Content(schema = @Schema(implementation = HttpServletResponse.class)),
                            description = "User unauthorized"
                    )
            })
    @GetMapping("/requests/to_users")
    public ResponseEntity<List<FriendToRequestDto>> getAllToFriendRequests(
            Authentication principal
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        List<FriendToRequestDto> friendFromRequestDtoList = friendshipService.getAllToFriendRequests(userDetails.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(friendFromRequestDtoList);
    }

    @Operation(summary = "Gets all friends of user",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The friends was successfully received"
                    ),
                    @ApiResponse(responseCode = "401",
                            content = @Content(schema = @Schema(implementation = HttpServletResponse.class)),
                            description = "User unauthorized"
                    )
            })
    @GetMapping
    public ResponseEntity<List<Long>> getAllFriendsId(
            Authentication principal
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        List<Long> friendRequestDtoList = friendshipService.getAllFriends(userDetails.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(friendRequestDtoList);
    }
}
