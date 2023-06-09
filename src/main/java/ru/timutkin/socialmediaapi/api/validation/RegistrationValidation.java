package ru.timutkin.socialmediaapi.api.validation;

import ru.timutkin.socialmediaapi.api.dto.SignupRequest;
import ru.timutkin.socialmediaapi.api.exception.InvalidRegistrationDataException;

import java.util.regex.Pattern;

public class RegistrationValidation {

    private RegistrationValidation() {
    }

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                                              "[a-zA-Z0-9_+&*-]+)*@" +
                                              "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                                              "A-Z]{2,7}$";

    public static void validate(SignupRequest request){
        if (request.getUsername() == null || request.getUsername().isBlank() || request.getUsername().length() <= 3 ||  request.getUsername().length() >= 21){
            throw new InvalidRegistrationDataException("The user name should not be empty, consist of spaces, have a length from 4 to 20");
        }
        if (request.getEmail() == null || !isValidEmail(request.getEmail()) || request.getEmail().length() > 50){
            throw new InvalidRegistrationDataException("Incorrect email format");
        }
        if (request.getPassword() == null || !validatePassword(request.getPassword()) ){
            throw new InvalidRegistrationDataException("The password is incorrect");
        }

    }

    private static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        if (email == null)
            return false;
        return pattern.matcher(email).matches();
    }

    public static boolean validatePassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";
        return password.matches(regex);
    }

}
