package ru.tinkoff.edu.java.bot.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.utils.MessageSender;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequiredArgsConstructor
public class BotController {

    private final MessageSender sender;

    @PostMapping(path = "/updates", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Отправить обновление")
    public ResponseEntity<String> updateLink(@Valid @RequestBody LinkUpdateRequest request) {
        for (long id : request.tgChatIds()) {
            sender.send(id, "responses/update_link.mustache", Map.of("link", request.url()));
        }
        return ResponseEntity.ok("Updates received!");
    }
}
