package ru.tinkoff.edu.java.scrapper.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.parser.url.results.GitHubResult;
import ru.tinkoff.edu.java.parser.url.results.StackOverflowResult;
import ru.tinkoff.edu.java.parser.url.results.UrlResult;
import ru.tinkoff.edu.java.scrapper.LinkManager;
import ru.tinkoff.edu.java.scrapper.dto.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class LinkUpdaterImpl implements LinkUpdater {

    private final LinkRepository repository;

    private final LinkManager manager;

    @Override
    public int update() {
        var oldLinks = repository.findAllOldLinks();
        for (Link l : oldLinks) {
            UrlResult result = manager.getLinkResult(l.getUrl()).get();
            if (result instanceof GitHubResult) {
                GitHubResponse response = manager.getGitHubRepositoryInformation((GitHubResult) result).get();
                log.info(response.fullName() + " " + response.updatedAt());
            } else {
                StackOverflowResponse response = manager
                        .getStackOverflowQuestionInformation((StackOverflowResult) result).get();
                log.info(response.answerCount() + " " + response.creationDate());
            }
            repository.updateCheckedAtTime(l.getId());
        }
        return 0;
    }
}
