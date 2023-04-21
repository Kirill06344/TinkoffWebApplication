package ru.tinkoff.edu.java.scrapper.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;

@Service
@Primary
@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {

    private final ChatRepository repository;

    @Override
    public void register(long tgChatId) {
        repository.add(new Chat(tgChatId));
    }

    @Override
    public int unregister(long tgChatId) {
        return repository.deleteById(tgChatId);
    }
}
