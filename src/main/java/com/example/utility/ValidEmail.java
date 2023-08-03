package com.example.utility;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE/*classes*/, FIELD, ANNOTATION_TYPE/*other annotations*/})//This annotation shows where @ValidEmail can be used
@Retention(RUNTIME)//this shows the period in which the annotation will be accessible
@Constraint(validatedBy = EmailValidator.class)//this annotation shows which class will make validations
@Documented
public @interface ValidEmail {
    String message() default "Invalid email";
    Class<?>[] groups() default {};//this method is used for grouping the validations
    Class<? extends Payload>[] payload() default {};//additional validation data
}