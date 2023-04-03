package ru.tinkoff.edu.java.bot.clients;


import ru.tinkoff.edu.java.bot.dto.ListLinksResponse;

import java.util.Optional;

public interface ScrapperClient {

    Optional<ListLinksResponse> getAllLinks(Long tgId);

}
