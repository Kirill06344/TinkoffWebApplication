package ru.tinkoff.edu.java.scrapper.services;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.clients.GitHubClient;
import ru.tinkoff.edu.java.scrapper.web.dto.GitHubResponse;

@Service
public class GitHubService {

    GitHubClient gitHubClient;

    public GitHubService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public Mono<GitHubResponse> getRepositoryInfoReactive(String owner, String repository) {
        return gitHubClient.fetchRepositoryInfo(owner, repository);
    }

}
