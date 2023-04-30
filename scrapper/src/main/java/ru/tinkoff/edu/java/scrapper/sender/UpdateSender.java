package ru.tinkoff.edu.java.scrapper.sender;

import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateRequest;

public interface UpdateSender {
    void send(LinkUpdateRequest request);
}
