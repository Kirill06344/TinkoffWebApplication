package ru.tinkoff.edu.java.scrapper.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatRepository;


@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {

    private final JdbcChatRepository repository;

    @Override
    public void register(long tgChatId) {
        repository.add(new Chat().setId(tgChatId));
    }

    @Override
    public int unregister(long tgChatId) {
        return repository.deleteById(tgChatId);
    }
}
