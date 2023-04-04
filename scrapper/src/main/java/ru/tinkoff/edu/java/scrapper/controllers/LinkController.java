package ru.tinkoff.edu.java.scrapper.controllers;

import lombok.extern.slf4j.Slf4j;
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
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.RemoveLinkRequest;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/links")
@Slf4j
public class LinkController {

    private static final String TG_HEADER = "Tg-Chat-Id";

    @GetMapping(produces = APPLICATION_JSON_VALUE)
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
        log.info("Request form tgId:" + tgChatId);
        return new ListLinksResponse(List.of(
                new LinkResponse(10L, "https://stackoverflow.com/questions/53182250"),
                new LinkResponse(12L, "https://github.com/spullara/mustache.java")
        ), 2);
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
        return new LinkResponse(1L,"https://github.com/spullara/mustache.java");
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
