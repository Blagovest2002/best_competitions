package com.example.utility;

import com.example.model.dto.UserLoginDto;
import com.example.model.dto.UserRegisterRequestDto;
import com.example.model.exception.BadRequestException;

public class ValidationUtility {

    public static boolean isValidFirstName(String firstName){
        return firstName.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$");
    }
    public static boolean isValidLastName(String lastName){
        return lastName.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$");
    }
    public static boolean isValidEmail(String email){
        return email.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
    }
    public static boolean isValidPhoneNumber(String phone){
        return phone.matches("^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$");
    }
    public static boolean isValidPassword(String password){
        //The password bust be minimum 8 characters, 1 Upper, 1 Lower,1 digit ,1 special char
        return password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
    }
    public static boolean arePasswordsMatching(String password1,String password2){
        return password1.equals(password2);
    }
    public static void isValidRegistration(UserRegisterRequestDto user){

        if(!isValidFirstName(user.getFirstName())){
            throw new BadRequestException(ValidationErrorMessages.NOT_VALID_FIRST_NAME.label);
        }
        if(!isValidLastName(user.getLastName())){
            System.out.println("Here ");
            throw new BadRequestException(ValidationErrorMessages.NOT_VALID_LAST_NAME.label);
        }
        if(!ValidationUtility.isValidPhoneNumber(user.getPhoneNumber())){
            throw new BadRequestException(ValidationErrorMessages.NOT_VALID_PHONE_NUMBER.label);
        }
        if(!ValidationUtility.arePasswordsMatching(user.getPassword(),user.getConfirmPassword())){
            throw new BadRequestException(ValidationErrorMessages.NOT_MATCHING_PASSWORDS.label);
        }
    }
     public static void checkValidLogin(UserLoginDto u){

     }


}
