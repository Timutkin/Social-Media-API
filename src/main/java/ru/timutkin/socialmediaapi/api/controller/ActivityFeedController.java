package ru.timutkin.socialmediaapi.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.timutkin.socialmediaapi.api.dto.PostDto;
import ru.timutkin.socialmediaapi.api.security.service.UserDetailsImpl;
import ru.timutkin.socialmediaapi.api.service.ActivityFeedService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(

        value = "/api/activity"
)
public class ActivityFeedController {

    private final ActivityFeedService activityFeedService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getFeedActivity(Pageable pageable,
                                                         Authentication principal
                                                   ){
        UserDetailsImpl userDetails = (UserDetailsImpl) principal.getPrincipal();
        List<PostDto> postDtoList =  activityFeedService.getFeedActivity(pageable, userDetails.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(postDtoList);
    }

}
