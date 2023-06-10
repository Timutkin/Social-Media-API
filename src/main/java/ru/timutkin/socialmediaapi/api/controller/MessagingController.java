package ru.timutkin.socialmediaapi.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.timutkin.socialmediaapi.api.security.service.UserDetailsImpl;
import ru.timutkin.socialmediaapi.api.service.MessagingService;
import ru.timutkin.socialmediaapi.api.validation.Validation;

@AllArgsConstructor
@RestController
@RequestMapping(
        value = "api/messages"
)
public class MessagingController {

    private final MessagingService messagingService;

    @PostMapping("/add/{userId}")
    public ResponseEntity<String> sendRequestToMessaging(@PathVariable Long userId,
                                                          Authentication principal
    ) {
        Validation.validateId(userId);
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        messagingService.sendRequestToMessaging(userId, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body("The messaging request has been sent successfully");
    }

}
