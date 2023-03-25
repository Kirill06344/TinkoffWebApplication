package ru.tinkoff.edu.java.scrapper.clients;

import java.util.Optional;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.web.dto.GitHubResponse;

@HttpExchange(url = "/repos/{owner}/{repo}", accept = "application/vnd.github.v3+json")
public interface GitHubClient {
    @GetExchange
    Mono<GitHubResponse> fetchRepositoryInfo(@PathVariable String owner, @PathVariable String repo);
}
