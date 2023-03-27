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
import ru.tinkoff.edu.java.scrapper.web.exceptions.InvalidRepositoryInformation;
import ru.tinkoff.edu.java.scrapper.web.exceptions.InvalidQuestionInformation;

@RestControllerAdvice(
    basePackages = "ru.tinkoff.edu.java.scrapper.web.controllers"
)
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleEMethodArgumentsException(Exception e) {
        return ResponseEntity.status(400)
            .body(
                ApiErrorResponse.builder()
                    .description("Некорректные параметры запроса")
                    .exceptionName(e.getClass().getSimpleName())
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

    @ExceptionHandler({InvalidRepositoryInformation.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponse> handleGitHubInvalidInfo(InvalidRepositoryInformation e) {
        return ResponseEntity.status(404)
            .body(
                ApiErrorResponse.builder()
                    .description("Проверьте еще раз, правильно ли указаны имя владельца и название репозитория")
                    .exceptionName(e.getClass().getSimpleName())
                    .exceptionMessage(e.getMessage())
                    .stacktrace(null)
                    .code("404")
                    .build()
            );
    }

    @ExceptionHandler({InvalidQuestionInformation.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponse> handleStackoverflowInfo(InvalidQuestionInformation e) {
        return ResponseEntity.status(404)
            .body(
                ApiErrorResponse.builder()
                    .description("Некорректно указан id вопроса, он должен быть в цифровом формате")
                    .exceptionName(e.getClass().getSimpleName())
                    .exceptionMessage(e.getMessage())
                    .stacktrace(null)
                    .code("404")
                    .build()
            );
    }

}
