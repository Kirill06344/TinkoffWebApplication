package ru.tinkoff.edu.java.scrapper.services.jdbc;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.services.TgChatService;


@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {

    private final ChatRepository repository;

    @Override
    public void register(long tgChatId) {
        repository.add(new Chat().setId(tgChatId));
    }

    @Override
    public int unregister(long tgChatId) {
        return repository.deleteById(tgChatId);
    }
}
