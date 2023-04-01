package ru.tinkoff.edu.java.scrapper.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import ru.tinkoff.edu.java.scrapper.dto.ApiErrorResponse;

@RestController
@RequestMapping(value = "/tg-chat/{id}")
public class ChatController {
    @PostMapping
    @Operation(summary = "Зарегистрировать чат",
        responses = {
            @ApiResponse(responseCode = "200", description = "Чат зарегестрирован"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
        }
    )
    public ResponseEntity<?> registerChat(@PathVariable("id") Long id) {
        return ResponseEntity.ok("Чат успешно зарегестрирован");
    }

    @DeleteMapping
    @Operation(summary = "Удалить чат",
        responses = {
            @ApiResponse(responseCode = "200", description = "Чат успешно удален"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Чат не существует",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE))
        }
    )
    public ResponseEntity<String> deleteChat(@PathVariable("id") Long id) {
        return ResponseEntity.ok("Чат успешно удален");
    }

}
