package ru.tinkoff.edu.java.scrapper.controllers;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.tinkoff.edu.java.scrapper.dto.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.exceptions.InvalidLink;
import ru.tinkoff.edu.java.scrapper.exceptions.NotExistingChat;
import ru.tinkoff.edu.java.scrapper.exceptions.NotTrackedLink;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleEMethodArgumentsException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
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
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .build()
            );
    }

    @ExceptionHandler({NotExistingChat.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponse> handleNotExistingChat(NotExistingChat ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
            .body(
                ApiErrorResponse.builder()
                    .description("Удаление чата с указанным id невозможно.")
                    .exceptionName(ex.getClass().getSimpleName())
                    .exceptionMessage(ex.getMessage())
                    .stacktrace(null)
                    .code(HttpStatus.NOT_FOUND.toString())
                    .build()
            );
    }

    @ExceptionHandler({InvalidLink.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleInvalidLink(InvalidLink ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
            .body(
                ApiErrorResponse.builder()
                    .description("Невозможно отслеживать данную ссылку!")
                    .exceptionName(ex.getClass().getSimpleName())
                    .exceptionMessage(ex.getMessage())
                    .stacktrace(null)
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .build()
            );
    }

    @ExceptionHandler({NotTrackedLink.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleNotTrackedLink(NotTrackedLink ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
            .body(
                ApiErrorResponse.builder()
                    .description("Вы итак не отслеживаете данную ссылку!")
                    .exceptionName(ex.getClass().getSimpleName())
                    .exceptionMessage(ex.getMessage())
                    .stacktrace(null)
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .build()
            );
    }

}
