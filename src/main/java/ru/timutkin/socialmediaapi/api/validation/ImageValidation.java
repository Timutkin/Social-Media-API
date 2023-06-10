package ru.timutkin.socialmediaapi.api.validation;

import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.timutkin.socialmediaapi.api.exception.InvalidFileException;

public class ImageValidation {

    private ImageValidation() {
    }

    public static void validateFiles(MultipartFile[] files) {
        if(files != null){
            for (MultipartFile file : files) {
                String extension = FileNameUtils.getExtension(file.getOriginalFilename());
                if (file.getSize() == 0) {
                    throw new InvalidFileException("File with name = " + file.getName() + " is empty");
                }
                if (files.length > 3) {
                    throw new InvalidFileException("Cannot upload more than three files");
                }
                if (file.getSize() > 3000000) {
                    throw new InvalidFileException("File size should be less than 3MB");
                }
                if (!isSupportedExtension(extension)) {
                    throw new InvalidFileException("Incorrect file format (valid format : png, jpg, jpeg)");
                }
            }
        }
    }

    public static void validateFile(MultipartFile file) {
        String extension = FileNameUtils.getExtension(file.getOriginalFilename());
        if (file.getSize() == 0) {
            throw new InvalidFileException("File with name = " + file.getName() + " is empty");
        }
        if (file.getSize() > 3000000) {
            throw new InvalidFileException("File size should be less than 3MB");
        }
        if (!isSupportedExtension(extension)) {
            throw new InvalidFileException("Incorrect file format (valid format : png, jpg, jpeg)");
        }
    }

    private static boolean isSupportedExtension(String extension) {
        return extension != null && (
                extension.equals("png")
                || extension.equals("jpg")
                || extension.equals("jpeg"));
    }
}
