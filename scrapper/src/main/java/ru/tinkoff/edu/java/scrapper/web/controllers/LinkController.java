package ru.tinkoff.edu.java.scrapper.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import ru.tinkoff.edu.java.scrapper.services.GitHubService;
import ru.tinkoff.edu.java.scrapper.web.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.web.dto.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.web.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.web.dto.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.web.dto.RemoveLinkRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/links")
public class LinkController {

    GitHubService gitHubService;

    public LinkController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получить все отслеживаемые ссылки",
        responses = {
            @ApiResponse(responseCode = "200", description = "Ссылки успешно получены")
        }
    )
    public ListLinksResponse getAllLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId) {
        return new ListLinksResponse(null, null);
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Добавить отслеживание ссылки")
    public LinkResponse addLink(@RequestHeader("Tg-Chat-Id") Long tgChatId,
                                @Valid @RequestBody AddLinkRequest request) {
        return new LinkResponse(null, null);
    }

    @DeleteMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Убрать отслеживание ссылки")
    public LinkResponse deleteLink(@RequestHeader("Tg-Chat-Id") Long tgChatId,
                                   @Valid @RequestBody RemoveLinkRequest request) {
        return new LinkResponse(null, null);
    }

    /**
     * Test controller to check GitHubResponse
     */
    @GetMapping("/github")
    public GitHubResponse getReposInformation(String owner,String repository) {
        System.out.println(owner + " " + repository);
        return gitHubService.getRepositoryInfoReactive(owner, repository).block();
    }

}
