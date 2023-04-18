package ru.tinkoff.edu.java.scrapper.clients;

import ru.tinkoff.edu.java.scrapper.dto.GitHubResponse;

import java.util.Optional;

public interface GitHubClient {
    Optional<GitHubResponse> fetchRepositoryInfo(String owner, String repos);

}
