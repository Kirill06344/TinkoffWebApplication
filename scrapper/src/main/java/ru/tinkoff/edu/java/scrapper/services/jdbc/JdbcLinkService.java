package ru.tinkoff.edu.java.scrapper.services.jdbc;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.parser.url.results.UrlResult;
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.services.LinkService;
import ru.tinkoff.edu.java.scrapper.utils.LinkManager;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.exceptions.InvalidLink;
import ru.tinkoff.edu.java.scrapper.exceptions.NotTrackedLink;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {

    private final LinkRepository linkRepository;

    private final ChatLinkRepository chatLinkRepository;

    private final LinkManager manager;

    @Override
    public Link add(long tgChatId, AddLinkRequest request) {
        UrlResult result = manager.checkLinkForExistence(request.link());
        Link link = manager.createLinkFromUrlResult(result, request.link());
        var response = linkRepository.add(link);
        if (response.isPresent()) {
            chatLinkRepository.add(new ChatLink(tgChatId, response.get().getId()));
            return response.get();
        }
        return response.get();
    }

    @Override
    public Link remove(long tgChatId, URI url) {
        var link = linkRepository.findLinkByUrl(url.toString());
        int deletedRows = chatLinkRepository.deleteById(new ChatLink(
            tgChatId,
            link.orElseThrow(() -> new InvalidLink(url.toString())).getId()
        ));
        if (deletedRows == 0) {
            throw new NotTrackedLink(url.toString());
        }
        return link.get();
    }

    @Override
    public List<Link> listAll(long tgChatId) {
        var results = chatLinkRepository.findAllChatLinksByChatId(tgChatId);
        List<Link> links = new ArrayList<>();
        for (var cl : results) {
            Optional<Link> link = linkRepository.findLinkById(cl.getLinkId());
            link.ifPresent(links::add);
        }
        return links;
    }

}
