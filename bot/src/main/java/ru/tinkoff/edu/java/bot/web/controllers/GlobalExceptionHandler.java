package ru.tinkoff.edu.java.bot.web.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ru.tinkoff.edu.java.bot.web.controllers.BotController;
import ru.tinkoff.edu.java.bot.web.dto.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(400)
            .body(
                ApiErrorResponse.builder()
                    .description("Некорректные параметры запроса")
                    .exceptionName(e.getClass().getName())
                    .exceptionMessage(e.getMessage())
                    .stacktrace(
                        Arrays.stream(e.getStackTrace())
                            .map(StackTraceElement::getClassName)
                            .collect(Collectors.toList())
                    )
                    .code("400")
                    .build()
            );
    }
}
