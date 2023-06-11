package ru.timutkin.socialmediaapi.api.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    String errorMessage;
}
