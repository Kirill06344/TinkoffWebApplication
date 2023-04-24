package ru.tinkoff.edu.java.scrapper.controllers;

import lombok.RequiredArgsConstructor;
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
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.scrapper.dto.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.exceptions.NotExistingChat;
import ru.tinkoff.edu.java.scrapper.services.JpaTgChatService;
import ru.tinkoff.edu.java.scrapper.services.TgChatService;

@Slf4j
@RestController
@RequestMapping(value = "/tg-chat/{id}")
@RequiredArgsConstructor
public class ChatController {

    private final JpaTgChatService chatService;

    @PostMapping
    @Operation(summary = "Зарегистрировать чат",
        responses = {
            @ApiResponse(responseCode = "200", description = "Чат зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
        }
    )
    public ResponseEntity<Long> registerChat(@PathVariable("id") Long id) {
        chatService.register(id);
        log.info("User with id {} successfully registered", id);
        return ResponseEntity.ok(id);
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
    public ResponseEntity<Long> deleteChat(@PathVariable("id") Long id) {
        if (chatService.unregister(id) == 0) {
            throw new NotExistingChat(id);
        }
        log.info("User with id {} successfully unregistered", id);
        return ResponseEntity.ok(id);
    }

}
