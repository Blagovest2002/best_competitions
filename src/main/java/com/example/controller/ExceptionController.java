package com.example.controller;

import com.example.model.dto.ErrorDto;
import com.example.model.exception.BadRequestException;
import com.example.model.exception.NotFoundException;
import com.example.model.exception.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
//Global Exception Handler
@ControllerAdvice
public abstract class ExceptionController  {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        System.out.println("Goes here" + ex);
        return setErrorDto(HttpStatus.BAD_REQUEST,ex);

    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto badRequestException(Exception e){

        System.out.println(e);
        return setErrorDto(HttpStatus.BAD_REQUEST,e);
    }
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto notFoundException(Exception e){
        return setErrorDto(HttpStatus.NOT_FOUND,e);
    }
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDto unauthorizedException(Exception e){
        return setErrorDto(HttpStatus.UNAUTHORIZED,e);
    }
    private static ErrorDto setErrorDto(HttpStatus status,Exception e){

        LocalDateTime now = LocalDateTime.now();
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMsg(e.getMessage());
        errorDto.setStatusCode(status);
        errorDto.setDateTime(now);
        return errorDto;
    }
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDto expiredJwtException(Exception e){
        System.out.println(e);
        return setErrorDto(HttpStatus.UNAUTHORIZED,e);
    }
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorDto> authException(Exception e){
        System.out.println(e);
        ErrorDto errorDto = setErrorDto(HttpStatus.UNAUTHORIZED,e);
        return new ResponseEntity<>(errorDto,HttpStatus.UNAUTHORIZED);
    }
}
