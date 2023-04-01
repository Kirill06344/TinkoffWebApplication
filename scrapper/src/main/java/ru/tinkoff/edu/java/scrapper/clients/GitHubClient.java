package ru.tinkoff.edu.java.scrapper.clients;

import ru.tinkoff.edu.java.scrapper.dto.GitHubResponse;

public interface GitHubClient {
    GitHubResponse fetchRepositoryInfo(String owner, String repos);

}
