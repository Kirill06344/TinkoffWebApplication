package ru.tinkoff.edu.java.scrapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.parser.handler.UrlHandler;
import ru.tinkoff.edu.java.parser.url.results.GitHubResult;
import ru.tinkoff.edu.java.parser.url.results.StackOverflowResult;
import ru.tinkoff.edu.java.parser.url.results.UrlResult;
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.services.GitHubService;
import ru.tinkoff.edu.java.scrapper.services.StackOverflowService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LinkValidator {

    private final GitHubService gitHubService;

    private final StackOverflowService stackOverflowService;
    private final UrlHandler handler = new UrlHandler();

    public Optional<UrlResult> getLinkResult(AddLinkRequest request) {
        return handler.getParseResult(request.link());
    }

    public boolean isExistingLink(UrlResult result) {
        if (result instanceof GitHubResult) {
            return isExistingGitHubLink((GitHubResult) result);
        } else {
            return isExistingStackoverflowLink((StackOverflowResult) result);
        }
    }

    private boolean isExistingGitHubLink(GitHubResult result) {
        return gitHubService.getRepositoryInfo(result.name(), result.repository()).isPresent();
    }

    private boolean isExistingStackoverflowLink(StackOverflowResult result) {
        return stackOverflowService.getQuestionInfo(result.id()).isEmpty();
    }

}
