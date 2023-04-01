package ru.tinkoff.edu.java.bot.controllers;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.tinkoff.edu.java.bot.dto.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
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
