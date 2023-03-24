package ru.tinkoff.edu.java.bot.web.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import ru.tinkoff.edu.java.bot.web.dto.LinkUpdateRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
public class BotController {

    @PostMapping(path = "/updates", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Отправить обновление")
    public ResponseEntity<?> updateLink(@Valid @RequestBody LinkUpdateRequest request) {
        return ResponseEntity.ok().body("OK");
    }
}
