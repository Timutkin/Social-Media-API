package ru.timutkin.socialmediaapi.api.validation;

import ru.timutkin.socialmediaapi.api.exception.InvalidDataException;

public class Validation {

    private Validation() {
    }

    public static void validateId(Long id){
        if (id <= 0) {
            throw new InvalidDataException("Id cannot be less than or equal to zero");
        }
    }

}
