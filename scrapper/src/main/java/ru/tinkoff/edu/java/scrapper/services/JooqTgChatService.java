package ru.tinkoff.edu.java.scrapper.services;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatRepository;

@RequiredArgsConstructor
public class JooqTgChatService implements TgChatService {

    private final JooqChatRepository repository;

    @Override
    public void register(long tgChatId) {
        repository.add(new Chat().setId(tgChatId));
    }

    @Override
    public int unregister(long tgChatId) {
        return repository.deleteById(tgChatId);
    }
}
