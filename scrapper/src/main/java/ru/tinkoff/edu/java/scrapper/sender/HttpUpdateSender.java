package ru.tinkoff.edu.java.scrapper.sender;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.clients.BotClient;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateRequest;

@RequiredArgsConstructor
public class HttpUpdateSender implements UpdateSender {

    private final BotClient botClient;

    @Override
    public void send(LinkUpdateRequest request) {
        botClient.sendUpdate(request);
    }
}
