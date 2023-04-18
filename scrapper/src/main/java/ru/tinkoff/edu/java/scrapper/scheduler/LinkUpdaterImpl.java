package ru.tinkoff.edu.java.scrapper.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.parser.url.results.GitHubResult;
import ru.tinkoff.edu.java.parser.url.results.StackOverflowResult;
import ru.tinkoff.edu.java.parser.url.results.UrlResult;
import ru.tinkoff.edu.java.scrapper.LinkManager;
import ru.tinkoff.edu.java.scrapper.clients.BotClient;
import ru.tinkoff.edu.java.scrapper.dto.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LinkUpdaterImpl implements LinkUpdater {

    private final String description = "An update came from the tracked link!";

    private final LinkRepository linkRepository;

    private final ChatLinkRepository chatLinkRepository;

    private final LinkManager manager;

    private final BotClient botClient;

    @Override
    public int update() {
        var oldLinks = linkRepository.findAllOldLinks();
        for (Link link : oldLinks) {
            UrlResult result = manager.getLinkResult(link.getUrl()).get();
            if (result instanceof GitHubResult) {
                GitHubResponse response = manager.getGitHubRepositoryInformation((GitHubResult) result).get();
                sendIfItUpdated(link, response.pushedAt());
            } else {
                StackOverflowResponse response = manager
                        .getStackOverflowQuestionInformation((StackOverflowResult) result).get();
                sendIfItUpdated(link, response.lastActivityDate());
            }
            linkRepository.updateCheckedAtTime(link.getId());
        }
        return 0;
    }

    private LinkUpdateRequest buildRequest(Link link) {
        List<Long> ids = chatLinkRepository.findAllChatLinksByLinkId(link.getId()).stream()
                .map(ChatLink::getChatId)
                .toList();

        return new LinkUpdateRequest(link.getId(), link.getUrl(), description, ids);
    }

    private void sendIfItUpdated(Link link,OffsetDateTime date) {
        int update = linkRepository.updateUpdatedAtTime(link.getId(),date
                .atZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime());
        if (update != 0) {
            botClient.sendUpdate(buildRequest(link));
        }
    }
}
