package ru.timutkin.socialmediaapi.api.validation;

import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.timutkin.socialmediaapi.api.exception.InvalidPostDataException;

public class PostValidation {

    public static void validatePostCreation(String header, String text, MultipartFile[] files) {
        validateHeaderAndText(header, text);
        validateFiles(files);
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

    public static void validateFiles(MultipartFile[] files) {
        for (MultipartFile file : files) {
            String extension = FileNameUtils.getExtension(file.getOriginalFilename());
            if (file.getSize() == 0) {
                throw new InvalidPostDataException("File with name = " + file.getName() + " is empty");
            }
            if (files.length > 3) {
                throw new InvalidPostDataException("Cannot upload more than three files");
            }
            if (file.getSize() > 3000000) {
                throw new InvalidPostDataException("File size should be less than 3MB");
            }
            if (!isSupportedExtension(extension)) {
                throw new InvalidPostDataException("Incorrect file format (valid format : png, jpg, jpeg)");
            }
        }
    }


    public static void validateId(Long id) {
        if (id <= 0) {
            throw new InvalidPostDataException("Id cannot be less than or equal to zero");
        }
    }

    private static boolean isSupportedExtension(String extension) {
        return extension != null && (
                extension.equals("png")
                || extension.equals("jpg")
                || extension.equals("jpeg"));
    }
}
