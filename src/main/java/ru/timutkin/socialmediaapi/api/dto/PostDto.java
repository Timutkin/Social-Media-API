package ru.timutkin.socialmediaapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto{
    private Long id;
    private Date created;
    private String header;
    private String text;
    private String authorUsername;
    private List<String> images;
}
