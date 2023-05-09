package ru.tinkoff.edu.java.scrapper.services;

import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.net.URI;
import java.util.List;

public interface LinkService {
    Link add(long tgChatId, AddLinkRequest request);

    Link remove(long tgChatId, URI url);

    List<Link> listAll(long tgChatId);
}
