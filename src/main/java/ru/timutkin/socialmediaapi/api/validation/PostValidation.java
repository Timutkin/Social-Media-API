package ru.timutkin.socialmediaapi.api.validation;

import org.springframework.web.multipart.MultipartFile;
import ru.timutkin.socialmediaapi.api.exception.InvalidPostDataException;

public class PostValidation {

    private PostValidation() {
    }

    public static void validatePostCreation(String header, String text, MultipartFile[] files) {
        validateHeaderAndText(header, text);
        ImageValidation.validateFiles(files);
    }

    public static void validateHeaderAndText(String header, String text) {
        if (header == null || header.isBlank()) {
            throw new InvalidPostDataException("The header should not be empty or consist of spaces");
        }
        if (text == null || text.isBlank()) {
            throw new InvalidPostDataException("The text should not be empty or consist of spaces");
        }
    }

    public static void validateHeaderAndTextUpdate(String header, String text) {
        if (header!= null && header.isBlank()) {
            throw new InvalidPostDataException("The header should not be empty or consist of spaces");
        }
        if (text != null && text.isBlank()) {
            throw new InvalidPostDataException("The text should not be empty or consist of spaces");
        }
    }

    public static void validateId(Long id) {
        if (id <= 0) {
            throw new InvalidPostDataException("Id cannot be less than or equal to zero");
        }
    }

}
