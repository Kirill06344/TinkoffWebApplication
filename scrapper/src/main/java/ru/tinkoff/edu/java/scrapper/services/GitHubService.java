package ru.tinkoff.edu.java.scrapper.services;

import org.springframework.stereotype.Service;

import ru.tinkoff.edu.java.scrapper.clients.GitHubClient;
import ru.tinkoff.edu.java.scrapper.dto.GitHubResponse;

import java.util.Optional;

@Service
public class GitHubService {

    private final GitHubClient client;

    public GitHubService(GitHubClient client) {
        this.client = client;
    }

    public Optional<GitHubResponse> getRepositoryInfo(String owner, String repos) {
        return client.fetchRepositoryInfo(owner, repos);
    }

}
