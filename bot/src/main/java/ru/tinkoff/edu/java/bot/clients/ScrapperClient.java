package ru.tinkoff.edu.java.bot.clients;

import ru.tinkoff.edu.java.bot.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.ListLinksResponse;

import java.util.Optional;

public interface ScrapperClient {

    Optional<Long> registerChat(Long tgId);

    Optional<ListLinksResponse> getAllLinks(Long tgId);

    Optional<LinkResponse> trackLink(Long tgId, String link);

    Optional<LinkResponse> untrackLink(Long tgId, String link);

}
