package ru.tinkoff.edu.java.scrapper.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.parser.url.results.GitHubResult;
import ru.tinkoff.edu.java.parser.url.results.StackOverflowResult;
import ru.tinkoff.edu.java.parser.url.results.UrlResult;
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.exceptions.InvalidLink;
import ru.tinkoff.edu.java.scrapper.exceptions.NotExistingChat;
import ru.tinkoff.edu.java.scrapper.exceptions.NotTrackedLink;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.utils.LinkManager;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JpaLinkService implements LinkService {

    private final JpaLinkRepository linkRepository;

    private final JpaChatRepository chatRepository;

    private final LinkManager manager;

    @Transactional
    @Override
    public Link add(long tgChatId, AddLinkRequest request) {
        UrlResult result = manager.checkLinkForExistence(request.link());
        Link link = manager.createLinkFromUrlResult(result, request.link());
        link = linkRepository.findByUrl(link.getUrl()).orElse(link);
        Chat chat = getChatById(tgChatId);
        link.getChats().add(chat);
        chat.getLinks().add(link);
        return linkRepository.save(link);
    }

    @Transactional
    @Override
    public Link remove(long tgChatId, URI url) {
        Link link = linkRepository.findByUrl(url.toString()).orElseThrow(() -> new NotTrackedLink(url.toString()));
        Chat chat = getChatById(tgChatId);
        if(!link.getChats().remove(chat) || !chat.getLinks().remove(link)) {
            throw new NotTrackedLink(link.getUrl());
        }
        return link;
    }

    private Chat getChatById(long tgChatId){
        return chatRepository.findById(tgChatId).orElseThrow(() -> new NotExistingChat(tgChatId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Link> listAll(long tgChatId) {
        return getChatById(tgChatId).getLinks().stream().toList();
    }

}