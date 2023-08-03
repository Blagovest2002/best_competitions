package com.example.utility;

import com.example.model.exception.BadRequestException;
import com.example.model.exception.UnauthorizedException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PasswordValidator implements ConstraintValidator<ValidPassword,String> {
private Pattern pattern;
private Matcher matcher;
public static final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if(!validatePassword(password)){
            throw new BadRequestException(ValidationErrorMessages.NOT_VALID_PASSWORD.label);
        }
      return true;
    }
    private boolean validatePassword(String password){
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher =  pattern.matcher(password);
        System.out.println(matcher.matches());
        return matcher.matches();
    }

}
