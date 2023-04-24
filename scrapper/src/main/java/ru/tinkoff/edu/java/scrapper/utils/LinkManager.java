package ru.tinkoff.edu.java.scrapper.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.parser.handler.UrlHandler;
import ru.tinkoff.edu.java.parser.url.results.GitHubResult;
import ru.tinkoff.edu.java.parser.url.results.StackOverflowResult;
import ru.tinkoff.edu.java.parser.url.results.UrlResult;
import ru.tinkoff.edu.java.scrapper.dto.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.services.GitHubService;
import ru.tinkoff.edu.java.scrapper.services.StackOverflowService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LinkManager {

    private final GitHubService gitHubService;

    private final StackOverflowService stackOverflowService;
    private final UrlHandler handler = new UrlHandler();

    public Optional<UrlResult> getLinkResult(String link) {
        return handler.getParseResult(link);
    }

    public boolean isExistingLink(UrlResult result) {
        if (result instanceof GitHubResult) {
            return isExistingGitHubLink((GitHubResult) result);
        } else {
            return isExistingStackoverflowLink((StackOverflowResult) result);
        }
    }

    public Optional<GitHubResponse> getGitHubRepositoryInformation(GitHubResult result) {
        return gitHubService.getRepositoryInfo(result.name(), result.repository());
    }

    public Optional<StackOverflowResponse> getStackOverflowQuestionInformation(StackOverflowResult result) {
        return stackOverflowService.getQuestionInfo(result.id());
    }

    private boolean isExistingGitHubLink(GitHubResult result) {
        return gitHubService.getRepositoryInfo(result.name(), result.repository()).isPresent();
    }

    private boolean isExistingStackoverflowLink(StackOverflowResult result) {
        return stackOverflowService.getQuestionInfo(result.id()).isPresent();
    }


}
