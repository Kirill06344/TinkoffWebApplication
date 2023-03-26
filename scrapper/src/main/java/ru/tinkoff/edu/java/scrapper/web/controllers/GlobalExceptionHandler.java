package ru.tinkoff.edu.java.scrapper.web.controllers;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import ru.tinkoff.edu.java.scrapper.web.dto.ApiErrorResponse;

@RestControllerAdvice (
    basePackages = "ru.tinkoff.edu.java.scrapper.web.controllers"
)
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
