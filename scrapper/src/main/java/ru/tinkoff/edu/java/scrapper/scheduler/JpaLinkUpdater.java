package ru.tinkoff.edu.java.scrapper.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.parser.url.results.GitHubResult;
import ru.tinkoff.edu.java.parser.url.results.StackOverflowResult;
import ru.tinkoff.edu.java.parser.url.results.UrlResult;
import ru.tinkoff.edu.java.scrapper.clients.BotClient;
import ru.tinkoff.edu.java.scrapper.dto.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.exceptions.InvalidLink;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.utils.ConverterToDateTime;
import ru.tinkoff.edu.java.scrapper.utils.DataChangeState;
import ru.tinkoff.edu.java.scrapper.utils.LinkManager;
import ru.tinkoff.edu.java.scrapper.utils.ServiceResponses;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class JpaLinkUpdater implements LinkUpdater{

    private final JpaLinkRepository linkRepository;

    private final LinkManager manager;

    private final BotClient botClient;

    @Override
    public int update() {
        log.info("New update:" + LocalDateTime.now());
        List<Link> oldLinks = linkRepository.findAllOldLinks(LocalDateTime.now());
        for (Link link : oldLinks) {
            log.info("Need to check for new information: " + link.getUrl());
            UrlResult result = manager.getLinkResult(link.getUrl()).get();
            if (result instanceof GitHubResult) {
                GitHubResponse response = manager.getGitHubRepositoryInformation((GitHubResult) result).get();
                sendIfItUpdated(link, response.pushedAt(), response.openIssues(), true);
            } else {
                StackOverflowResponse response = manager
                        .getStackOverflowQuestionInformation((StackOverflowResult) result).get();
                sendIfItUpdated(link, response.lastActivityDate(), response.answerCount(), false);
            }
            linkRepository.save(link.setCheckedAt(LocalDateTime.now()));
        }
        return oldLinks.size();
    }


    //Добавить исключение вместо InvalidLink
    private void sendIfItUpdated(Link link, OffsetDateTime date, long count, boolean isGit) {
        Link oldLink = linkRepository.findById(link.getId()).orElseThrow(() -> new InvalidLink(link.getUrl()));
        link.setUpdatedAt(ConverterToDateTime.convertOffset(date)).setIntersectingCountField(count);
        linkRepository.save(link);
        Link newLink = linkRepository.findById(link.getId()).orElseThrow(() -> new InvalidLink(link.getUrl()));

        DataChangeState state = compareLinks(oldLink, newLink);
        if (state != DataChangeState.NOTHING) {
            log.info("Send notification about update...");
            botClient.sendUpdate(buildRequest(newLink, isGit
                    ? ServiceResponses.getGithubResponse(state.toString())
                    : ServiceResponses.getStackOverflowResponses(state.toString())
            ));
        }
    }

    private LinkUpdateRequest buildRequest(Link link, String description) {
        return new LinkUpdateRequest(
                link.getId(),
                link.getUrl(),
                description,
                link.getChats().stream().map(Chat::getId).toList()
        );
    }

    private DataChangeState compareLinks(Link oldLink, Link newLink) {
        if (oldLink.getIntersectingCountField() < newLink.getIntersectingCountField()) {
            return DataChangeState.COUNT;
        } else if (!oldLink.getUpdatedAt().equals(newLink.getUpdatedAt())) {
            return DataChangeState.OTHER;
        } else {
            return DataChangeState.NOTHING;
        }
    }
}
