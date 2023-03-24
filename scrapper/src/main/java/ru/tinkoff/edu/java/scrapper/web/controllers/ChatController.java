package ru.tinkoff.edu.java.scrapper.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = "/tg-chat/{id}")
public class ChatController {
    @PostMapping
    @Operation(summary = "Зарегистрировать чат",
        responses = {
            @ApiResponse(responseCode = "200", description = "Чат зарегестрирован")
        }
    )
    public ResponseEntity<?> registerChat(@PathVariable("id") Long id) {
        return ResponseEntity.ok("Чат успешно зарегестрирован");
    }

    @DeleteMapping
    @Operation(summary = "Удалить чат")
    public ResponseEntity<String> deleteChat(@PathVariable("id") Long id) {
        return ResponseEntity.ok("Чат успешно удален");
    }

}
