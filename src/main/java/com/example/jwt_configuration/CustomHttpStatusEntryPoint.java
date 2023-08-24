package com.example.jwt_configuration;

import com.example.model.dto.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.parameters.P;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@RequiredArgsConstructor
@Component
public class CustomHttpStatusEntryPoint implements AuthenticationEntryPoint {
    private   HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    private  final HandlerExceptionResolver handlerExceptionResolver;
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json"); // Set the content type to JSON
        response.setCharacterEncoding("UTF-8"); // Set the character encoding

        // Create a JSON object with your custom error message
        String errorMessage = "Unauthorized: " + authException.getMessage();
        ErrorDto errorDto = new ErrorDto();
        errorDto.setDateTime(LocalDateTime.now());
        errorDto.setMsg(errorMessage);
        errorDto.setStatusCode(HttpStatus.UNAUTHORIZED);


        // Write the JSON response to the output stream
        String json = objectMapper.writeValueAsString(errorDto);
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
    }
}
