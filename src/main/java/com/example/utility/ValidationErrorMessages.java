package com.example.utility;

public enum ValidationErrorMessages {
    NOT_VALID_EMAIl("This email is not valid!"),
    NOT_VALID_PASSWORD("This password is not strong enough"),
    NOT_MATCHING_PASSWORDS("These passwords are different"),
    NOT_VALID_FIRST_NAME("The first name is not valid"),
    NOT_VALID_LAST_NAME("The last name is not valid"),
    NOT_VALID_PHONE_NUMBER("The phone number format is wrong"),
    EMAIL_ALREADY_EXISTS("The email already exists"),
    PHONE_NUMBER_ALREADY_EXISTS("The phone number already exists"),
    WRONG_PASSWORD("Password is wrong");
    public final String label;
    private ValidationErrorMessages(String label){

        this.label = label;
    }

}
