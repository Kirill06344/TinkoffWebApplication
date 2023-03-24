package ru.tinkoff.edu.java.scrapper.clients;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import ru.tinkoff.edu.java.scrapper.web.dto.GitHubResponse;

@Component
public class GitHubClientImpl implements GitHubClient {

    private WebClient webClient;

    public GitHubClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Optional<GitHubResponse> fetchRepositoryInfo(String owner, String repo) {
        return Optional.empty();
    }
}
