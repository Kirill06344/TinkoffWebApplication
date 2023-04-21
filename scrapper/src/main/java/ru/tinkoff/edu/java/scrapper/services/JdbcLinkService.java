package ru.tinkoff.edu.java.scrapper.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.exceptions.NotExistingChat;
import ru.tinkoff.edu.java.scrapper.jdbc.JdbcChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.jdbc.JdbcLinkRepository;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {

    private final JdbcLinkRepository linkRepository;

    private final JdbcChatLinkRepository chatLinkRepository;

    @Override
    public Link add(long tgChatId, URI url, LocalDateTime updatedAt) {
        var link = linkRepository.add(new Link().setUrl(url.toString()).setCheckedAt(LocalDateTime.now()).setUpdatedAt(updatedAt));
        if (link.isPresent()) {
            chatLinkRepository.add(new ChatLink(tgChatId, link.get().getId()));
            return link.get();
        }
        return link.get();
    }

    @Override
    public Optional<Link> remove(long tgChatId, URI url) {
        var link = linkRepository.findLinkByUrl(url.toString());
        link.ifPresent(value -> chatLinkRepository.deleteById(new ChatLink(tgChatId, value.getId())));
        return link;
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
