package ru.tinkoff.edu.java.bot.controllers;


import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class BotController {

    private final MessageSender sender;

    private final TelegramBot bot;

    @PostMapping(path = "/updates", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Отправить обновление")
    public ResponseEntity<String> updateLink(@Valid @RequestBody LinkUpdateRequest request) {
        for (long id : request.tgChatIds()) {
            log.info(String.valueOf(id));
            bot.execute(sender.send(id, "responses/update_link.mustache",
                    Map.of("msg", request.description(),
                    "link", request.url())));
        }
        return ResponseEntity.ok("Updates received!");
    }
}
