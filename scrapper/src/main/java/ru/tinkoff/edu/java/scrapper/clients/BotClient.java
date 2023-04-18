package ru.tinkoff.edu.java.scrapper.clients;

import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateRequest;

public interface BotClient {

    void sendUpdate(LinkUpdateRequest request);
}
