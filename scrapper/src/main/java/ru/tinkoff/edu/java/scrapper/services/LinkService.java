package ru.tinkoff.edu.java.scrapper.services;

import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkService {
    Link add(long tgChatId, Link link);
    Link remove(long tgChatId, URI url);
    List<Link> listAll(long tgChatId);
}
