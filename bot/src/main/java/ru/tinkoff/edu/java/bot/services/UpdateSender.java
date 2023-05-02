package ru.tinkoff.edu.java.bot.services;

import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;

public interface UpdateSender {
    void send(LinkUpdateRequest request);
}
