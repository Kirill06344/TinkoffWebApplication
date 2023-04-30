package ru.tinkoff.edu.java.scrapper.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.parser.url.results.GitHubResult;
import ru.tinkoff.edu.java.parser.url.results.StackOverflowResult;
import ru.tinkoff.edu.java.parser.url.results.UrlResult;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.sender.UpdateSender;
import ru.tinkoff.edu.java.scrapper.utils.ConverterToDateTime;
import ru.tinkoff.edu.java.scrapper.utils.DataChangeState;
import ru.tinkoff.edu.java.scrapper.utils.LinkManager;
import ru.tinkoff.edu.java.scrapper.clients.BotClient;
import ru.tinkoff.edu.java.scrapper.dto.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.utils.ServiceResponses;
import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JdbcLinkUpdater implements LinkUpdater {

    private final LinkRepository linkRepository;

    private final ChatLinkRepository chatLinkRepository;

    private final LinkManager manager;

    private final UpdateSender sender;

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
        return oldLinks.size();
    }

    private LinkUpdateRequest buildRequest(Link link, String description) {
        List<Long> ids = chatLinkRepository.findAllChatLinksByLinkId(link.getId()).stream()
                .map(ChatLink::getChatId)
                .toList();

        return new LinkUpdateRequest(link.getId(), link.getUrl(), description, ids);
    }

    private void sendIfItUpdated(Link link, OffsetDateTime date, long count, boolean isGit) {
        var state = linkRepository.updateOtherData(link.getId(), ConverterToDateTime.convertOffset(date), count);
        if (state == DataChangeState.NOTHING) {
            return;
        }
        sender.send(buildRequest(link, isGit
                ? ServiceResponses.getGithubResponse(state.toString())
                : ServiceResponses.getStackOverflowResponses(state.toString())
        ));
    }
}
