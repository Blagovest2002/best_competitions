package com.example.model.exception;

import jakarta.validation.ConstraintDeclarationException;

public class BadRequestException extends ConstraintDeclarationException {
    public BadRequestException(String msg){
        super(msg);
        System.out.println(msg);
    }
}
