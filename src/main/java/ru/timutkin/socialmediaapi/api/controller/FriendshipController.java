package ru.timutkin.socialmediaapi.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.timutkin.socialmediaapi.api.dto.FriendRequestDto;
import ru.timutkin.socialmediaapi.api.security.service.UserDetailsImpl;
import ru.timutkin.socialmediaapi.api.service.FriendshipService;
import ru.timutkin.socialmediaapi.api.validation.Validation;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(
        value = "/api/friends"
)
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping("/add/{userId}")
    public ResponseEntity<String> sendRequestToFriendship(@PathVariable Long userId,
                                                          Authentication principal
    ) {
        Validation.validateId(userId);
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        friendshipService.sendRequestToFriendShip(userId, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body("The friend request has been sent successfully");
    }

    @PostMapping("/remove/{userId}")
    public ResponseEntity<String> removeRequestToFriendship(@PathVariable Long userId,
                                                            Authentication principal
    ) {
        Validation.validateId(userId);
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        friendshipService.removeRequestToFriendShip(userId, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body("The friend request has been removed successfully");
    }

    @PostMapping("/receive/{fromUserId}")
    public ResponseEntity<String> receiveFriendRequest(@PathVariable Long fromUserId,
                                                       Authentication principal
                                                       ){
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        friendshipService.receiveFriendRequest(fromUserId, userDetails.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("The user has been successfully added to friends");
    }

    @PostMapping("/reject/{fromUserId}")
    public ResponseEntity<String> rejectFriendRequest(@PathVariable Long fromUserId,
                                                       Authentication principal
    ){
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        friendshipService.rejectFriendRequest(fromUserId, userDetails.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("The user has been successfully rejected to friends");
    }

    @GetMapping
    public ResponseEntity<List<FriendRequestDto>> getAllFriendRequests(Authentication principal) {
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        List<FriendRequestDto> friendRequestDtoList = friendshipService.getAllFriendRequests(userDetails.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(friendRequestDtoList);
    }

}
