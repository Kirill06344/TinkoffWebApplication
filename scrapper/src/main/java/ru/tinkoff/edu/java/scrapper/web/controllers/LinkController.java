package ru.tinkoff.edu.java.scrapper.web.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import ru.tinkoff.edu.java.scrapper.web.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.web.dto.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.web.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.web.dto.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.web.dto.RemoveLinkRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/links")
public class LinkController {

    private static final String TG_HEADER = "Tg-Chat-Id";

    @GetMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получить все отслеживаемые ссылки",
        responses = {
            @ApiResponse(responseCode = "200", description = "Ссылки успешно получены"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
        }
    )
    public ListLinksResponse getAllLinks(@RequestHeader(TG_HEADER) Long tgChatId) {
        return new ListLinksResponse(null, null);
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Добавить отслеживание ссылки",
        responses = {
            @ApiResponse(responseCode = "200", description = "Ссылка успешно добавлена"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
        }
    )
    public LinkResponse addLink(@RequestHeader(TG_HEADER) Long tgChatId,
                                @Valid @RequestBody AddLinkRequest request) {
        return new LinkResponse(null, null);
    }

    @DeleteMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Убрать отслеживание ссылки",
        responses = {
            @ApiResponse(responseCode = "200", description = "Ссылка успешно убрана"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(responseCode = "404", description = "Ссылка не найдена",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
        }
    )
    public LinkResponse deleteLink(@RequestHeader(TG_HEADER) Long tgChatId,
                                   @Valid @RequestBody RemoveLinkRequest request) {
        return new LinkResponse(null, null);
    }
}
