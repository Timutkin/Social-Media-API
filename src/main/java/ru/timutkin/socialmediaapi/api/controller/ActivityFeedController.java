package ru.timutkin.socialmediaapi.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.timutkin.socialmediaapi.api.constant.ApiConstant;
import ru.timutkin.socialmediaapi.api.dto.PostDto;
import ru.timutkin.socialmediaapi.api.security.service.UserDetailsImpl;
import ru.timutkin.socialmediaapi.api.service.ActivityFeedService;

import java.util.List;

@Tag(name = "An activity feed management APIs")
@AllArgsConstructor
@RestController
@RequestMapping(

        value = ApiConstant.VERSION_API+"/activity"
)
public class ActivityFeedController {

    private final ActivityFeedService activityFeedService;

    @Operation(summary = "Gets all post of user which are subscribed to by an authorized user",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The all posts was successfully received"
                    ),
                    @ApiResponse(responseCode = "401",
                            content = @Content(schema = @Schema(implementation = HttpServletResponse.class)),
                            description = "User unauthorized"
                    )
            })
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
