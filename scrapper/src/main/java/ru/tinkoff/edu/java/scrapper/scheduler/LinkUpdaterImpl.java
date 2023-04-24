package ru.tinkoff.edu.java.scrapper.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.parser.url.results.GitHubResult;
import ru.tinkoff.edu.java.parser.url.results.StackOverflowResult;
import ru.tinkoff.edu.java.parser.url.results.UrlResult;
import ru.tinkoff.edu.java.scrapper.utils.DataChangeState;
import ru.tinkoff.edu.java.scrapper.utils.LinkManager;
import ru.tinkoff.edu.java.scrapper.clients.BotClient;
import ru.tinkoff.edu.java.scrapper.dto.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LinkUpdaterImpl implements LinkUpdater {

    private static final Map<String, String> githubResponses = Map.of(
            "count", "Oooooh..someone open an issue or PR at this repository...",
            "other", "Hey! There is a new commit at this repository..."
    );

    private static final Map<String, String> stackOverFlowResponses = Map.of(
            "count", "Oooooh..someone left answer on this question...",
            "other", "Hey! There is a new modification in this question..."
    );


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
                sendIfItUpdated(link, response.pushedAt(), response.openIssues(), true);
            } else {
                StackOverflowResponse response = manager
                        .getStackOverflowQuestionInformation((StackOverflowResult) result).get();
                sendIfItUpdated(link, response.lastActivityDate(), response.answerCount(), false);
            }
            linkRepository.updateCheckedAtTime(link.getId());
        }
        return 0;
    }

    private LinkUpdateRequest buildRequest(Link link, String description) {
        List<Long> ids = chatLinkRepository.findAllChatLinksByLinkId(link.getId()).stream()
                .map(ChatLink::getChatId)
                .toList();

        return new LinkUpdateRequest(link.getId(), link.getUrl(), description, ids);
    }

    private void sendIfItUpdated(Link link, OffsetDateTime date, long count, boolean isGit) {
        var state = linkRepository.updateOtherData(link.getId(), convertOffset(date), count);
        if (state == DataChangeState.NOTHING) {
            return;
        }
        botClient.sendUpdate(buildRequest(link, isGit ? githubResponses.get(state.toString()) : stackOverFlowResponses.get(state.toString())));
    }

    private LocalDateTime convertOffset(OffsetDateTime time) {
        return time.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }
}
