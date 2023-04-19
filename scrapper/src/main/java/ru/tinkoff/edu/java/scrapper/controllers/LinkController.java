package ru.tinkoff.edu.java.scrapper.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import ru.tinkoff.edu.java.parser.url.results.GitHubResult;
import ru.tinkoff.edu.java.parser.url.results.StackOverflowResult;
import ru.tinkoff.edu.java.parser.url.results.UrlResult;
import ru.tinkoff.edu.java.scrapper.LinkManager;
import ru.tinkoff.edu.java.scrapper.dto.*;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.services.JdbcLinkService;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
@Slf4j
public class LinkController {

    private final JdbcLinkService linkService;

    private final LinkManager manager;

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
        var links = linkService.listAll(tgChatId).stream().map(l -> new LinkResponse(l.getId(), l.getUrl())).toList();
        return new ListLinksResponse(links, links.size());
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
    public ResponseEntity<LinkResponse> addLink(@RequestHeader(TG_HEADER) Long tgChatId,
                                                @Valid @RequestBody AddLinkRequest request) {
        var result = manager.getLinkResult(request.link());
        if (result.isEmpty() || !manager.isExistingLink(result.get())) {
            return ResponseEntity.badRequest().body(new LinkResponse(0L, request.link()));
        }

        UrlResult parseResult = result.get();
        LocalDateTime updatedAt;
        long count;
        if (parseResult instanceof GitHubResult) {
            var data = manager.getGitHubRepositoryInformation((GitHubResult) parseResult);
            updatedAt = data.get().pushedAt().atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
            count = data.get().openIssues();
        } else {
            var data = manager.getStackOverflowQuestionInformation((StackOverflowResult) parseResult);
            updatedAt = data.get().lastActivityDate().atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
            count = data.get().answerCount();
        }

        var link = linkService.add(tgChatId, new Link().setUrl(request.link()).setUpdatedAt(updatedAt).setIntersectingCountField(count));
        return ResponseEntity.ok(new LinkResponse(link.getId(), link.getUrl()));
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
    public ResponseEntity<LinkResponse> deleteLink(@RequestHeader(TG_HEADER) Long tgChatId,
                                                   @Valid @RequestBody RemoveLinkRequest request) {
        try {
            URI uri = new URI(request.link());
            var link = linkService.remove(tgChatId, uri);
            if (link.isPresent()) {
                return ResponseEntity.ok(new LinkResponse(link.get().getId(), link.get().getUrl()));
            }
        } catch (URISyntaxException ex) {
            log.info("URI exception!");
        }
        return ResponseEntity.badRequest().body(null);
    }
}
