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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        value = ApiConstant.VERSION_API + "/activity"
)
public class ActivityFeedController {

    private final ActivityFeedService activityFeedService;

    @Operation(summary = "Gets all post of user which are subscribed to an authorized user",
            description = """
                    page: the index of page that we want to retrieve – the parameter is zero-indexed and its default value is 0
                    size: the number of pages that we want to retrieve – the default value is 20
                    sort: one or more properties that we can use for sorting the results, using the following format: 
                    property1,property2(,asc|desc) – for instance, ?sort=name&sort=email,asc
                    """,
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
    public ResponseEntity<List<PostDto>> getFeedActivity(
            Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl user
    ) {
        List<PostDto> postDtoList = activityFeedService.getFeedActivity(pageable, user.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(postDtoList);
    }

}
