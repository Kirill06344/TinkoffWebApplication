package ru.tinkoff.edu.java.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.clients.ScrapperClient;
import ru.tinkoff.edu.java.bot.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.ListLinksResponse;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScrapperService {

    private final ScrapperClient client;

    public Optional<ListLinksResponse> getAllLinks(Long tgId) {
        return client.getAllLinks(tgId);
    }

    public Optional<LinkResponse> trackLink(Long tgId, String link) {
        return client.trackLink(tgId, link);
    }

    public Optional<LinkResponse> untrackLInk(Long tgId, String link) {
        return client.untrackLink(tgId, link);
    }

}
