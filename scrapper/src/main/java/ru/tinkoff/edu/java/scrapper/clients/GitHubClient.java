package ru.tinkoff.edu.java.scrapper.clients;

import java.util.Optional;

import ru.tinkoff.edu.java.scrapper.web.dto.GitHubResponse;

public interface GitHubClient {
    Optional<GitHubResponse> fetchRepositoryInfo(String owner, String repo);
}
