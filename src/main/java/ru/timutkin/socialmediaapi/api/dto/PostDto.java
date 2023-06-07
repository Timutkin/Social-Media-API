package ru.timutkin.socialmediaapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto{
    private Long id;
    private String header;
    private String text;
    private Long authorId;
    private List<Long> imagesId;
}
